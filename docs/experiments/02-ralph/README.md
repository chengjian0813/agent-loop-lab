# 02 · Ralph：一行 while true，让 Agent 跑完一个功能

**目的**：复现 Ralph 模式：同一份 `PROMPT.md` 反复喂给一个全新上下文的 Agent，状态全部放在文件里（`fix_plan.md` + git），每轮只做一件事。

**要做的功能**：优惠券有效期 + 品类券（见 `fix_plan.md`，一共 5 项）。

**步骤**

```bash
git switch -c exp/02-ralph
cp docs/experiments/02-ralph/PROMPT.md docs/experiments/02-ralph/fix_plan.md .
# 把 AGENT_CMD 换成你用的 headless 命令
AGENT_CMD='claude -p --dangerously-skip-permissions' MAX_ITERS=15 \
  bash docs/experiments/02-ralph/ralph.sh
```

**观察点**：跑了几轮、每轮是否只勾掉一项、有没有「勾了但没做」、最终 verify 是否通过。

**安全提示**：跳过权限确认的参数只在一次性分支 / 容器里用。
