#!/usr/bin/env bash
# Agent loop with brakes: iteration cap, wall-clock cap, no-progress cap, STOP file.
set -uo pipefail
: "${AGENT_CMD:?set AGENT_CMD to your headless agent command}"
MAX_ITERS="${MAX_ITERS:-10}"
MAX_MINUTES="${MAX_MINUTES:-60}"
NO_PROGRESS_LIMIT="${NO_PROGRESS_LIMIT:-2}"
PROMPT_FILE="${PROMPT_FILE:-PROMPT.md}"
DONE_CMD="${DONE_CMD:-./mvnw -B -q verify}"

start=$(date +%s); stale=0
state() { { git rev-parse HEAD; git status --porcelain; } | sha1sum | cut -d' ' -f1; }
log() { echo "[$(date +%H:%M:%S)] $*" | tee -a loop.log; }

for i in $(seq 1 "$MAX_ITERS"); do
  [ -f STOP ] && { log "STOP file found, stopping"; exit 3; }
  elapsed=$(( ($(date +%s) - start) / 60 ))
  [ "$elapsed" -ge "$MAX_MINUTES" ] && { log "time cap ${MAX_MINUTES}m reached"; exit 4; }

  before=$(state)
  log "iteration $i/$MAX_ITERS (elapsed ${elapsed}m)"
  $AGENT_CMD "$(cat "$PROMPT_FILE")" || log "agent exited non-zero"

  if [ "$(state)" = "$before" ]; then
    stale=$((stale + 1)); log "no progress ($stale/$NO_PROGRESS_LIMIT)"
    [ "$stale" -ge "$NO_PROGRESS_LIMIT" ] && { log "no-progress cap reached"; exit 5; }
  else
    stale=0
  fi

  if eval "$DONE_CMD" && scripts/check-test-integrity.sh "${BASE_REF:-origin/main}" >/dev/null; then
    log "done condition met after $i iterations"; exit 0
  fi
done
log "iteration cap $MAX_ITERS reached"; exit 2
