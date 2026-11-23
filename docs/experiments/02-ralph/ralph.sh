#!/usr/bin/env bash
# Minimal Ralph loop. State lives in fix_plan.md + git, not in the agent's context.
set -uo pipefail
: "${AGENT_CMD:?set AGENT_CMD, e.g. 'claude -p --dangerously-skip-permissions'}"
MAX_ITERS="${MAX_ITERS:-15}"

for i in $(seq 1 "$MAX_ITERS"); do
  echo "=== iteration $i / $MAX_ITERS ==="
  $AGENT_CMD "$(cat PROMPT.md)" || echo "agent exited non-zero"
  if grep -q '^ALL_DONE' fix_plan.md && ./mvnw -B -q verify; then
    echo "done after $i iterations"; exit 0
  fi
done
echo "hit MAX_ITERS=$MAX_ITERS without ALL_DONE"; exit 1
