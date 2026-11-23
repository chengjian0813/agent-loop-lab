# 07 · 一晚上烧掉多少额度？给 loop 装刹车

**目的**：比较无上限的 loop 和带刹车的 loop。刹车包括迭代上限、时间上限、连续无进展上限、人工停止文件。

**种子**：`loop-with-brakes.sh`。它是 02 `ralph.sh` 的加强版，可以直接替换。

```bash
AGENT_CMD='<your headless agent cmd>' MAX_ITERS=10 MAX_MINUTES=60 NO_PROGRESS_LIMIT=2 \
  bash docs/experiments/07-budget-caps/loop-with-brakes.sh
# 想随时叫停：touch STOP
```

**无进展的判定**：一轮结束后工作区和 HEAD 都没有变化（按 `git rev-parse HEAD` + `git status --porcelain` 的哈希判断）。

**观察点**：每种刹车分别在什么时候触发；同一个任务有刹车和无刹车时的总轮数、时长和花费（以工具账单为准，没有就写「未测」）。
