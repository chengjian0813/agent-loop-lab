#!/usr/bin/env bash
# Test-integrity guard: detects the usual ways an agent "makes tests pass" without fixing code.
#
#   scripts/check-test-integrity.sh [BASE_REF]      (default: $BASE_REF or origin/main)
#
# Compares the working tree (committed + uncommitted tracked changes) against BASE_REF and fails if:
#   1. a test file under src/test was deleted
#   2. the number of @Test methods went down
#   3. new skip markers were added (@Disabled, @Ignore, assumeTrue(false), @DisabledIf..., etc.)
#   4. pom.xml touched test-skipping or coverage settings
#   5. protected files changed (this script, CI workflows)
# Exit code 0 = clean, 1 = suspicious changes found, 2 = usage error.
set -euo pipefail

BASE="${1:-${BASE_REF:-origin/main}}"
if ! git rev-parse --verify --quiet "$BASE^{commit}" >/dev/null; then
  echo "error: base ref '$BASE' not found (try: git fetch origin main)" >&2
  exit 2
fi

fail=0
report() { echo "✗ $1"; fail=1; }

# 1. deleted test files
deleted=$(git diff --name-only --diff-filter=D "$BASE" -- src/test || true)
[ -n "$deleted" ] && report "deleted test files:"$'\n'"$(sed 's/^/    /' <<<"$deleted")"

# 2. @Test count must not decrease
count_tests() { grep -E '@Test([^A-Za-z]|$)' | wc -l | tr -d ' '; }
before=$(git grep -h -E '@Test([^A-Za-z]|$)' "$BASE" -- 'src/test' 2>/dev/null | wc -l | tr -d ' ')
after=$( (git ls-files -co --exclude-standard -- src/test | while read -r f; do [ -f "$f" ] && cat "$f"; done) | count_tests)
if [ "$after" -lt "$before" ]; then
  report "@Test count decreased: $before -> $after"
fi

# 3. new skip markers in tests (tracked diff + untracked new test files)
skip_re='@Disabled|@Ignore|@DisabledIf|@EnabledIf|@DisabledOn|@EnabledOn|assumeTrue\(\s*false|assumeFalse\(\s*true|Assumptions\.abort'
added=$(git diff "$BASE" -- src/test | grep -E '^\+' | grep -vE '^\+\+\+' | grep -E "$skip_re" || true)
untracked=$(git ls-files -o --exclude-standard -- src/test | xargs -r grep -nE "$skip_re" || true)
[ -n "$added$untracked" ] && report "new skip markers in tests:"$'\n'"$(printf '%s\n%s' "$added" "$untracked" | sed '/^$/d;s/^/    /')"

# 4. pom.xml: test skipping / coverage gate tampering
pom=$(git diff "$BASE" -- pom.xml | grep -E '^[+-]' | grep -vE '^(\+\+\+|---)' \
  | grep -E 'skipTests|maven\.test\.skip|testFailureIgnore|coverage\.line\.minimum|<minimum>|<exclude>|<skip>|haltOnFailure' || true)
[ -n "$pom" ] && report "pom.xml changes to test/coverage settings:"$'\n'"$(sed 's/^/    /' <<<"$pom")"

# 5. protected paths
protected=$(git diff --name-only "$BASE" -- scripts/check-test-integrity.sh .github/workflows || true)
[ -n "$protected" ] && report "protected files changed (needs human review):"$'\n'"$(sed 's/^/    /' <<<"$protected")"

if [ "$fail" -eq 0 ]; then
  echo "✓ test integrity OK vs $BASE (@Test: $before -> $after)"
fi
exit "$fail"
