# 实验索引 / Experiments

每篇文章对应一个目录。每个实验都从 `main` 的绿色基线开始，读者按目录里的 README 复现即可。

| # | 文章主题 | 目录 | 种子文件 |
|---|---|---|---|
| 1 | Loop 到底是什么 | [01-what-is-a-loop](01-what-is-a-loop/) | 对照任务说明 |
| 2 | Ralph：while true 跑完一个功能 | [02-ralph](02-ralph/) | `PROMPT.md`、`fix_plan.md`、`ralph.sh` |
| 3 | 停止条件与防作弊 | [03-stop-conditions](03-stop-conditions/) | `TASK.md`、`AllocationSpecTest.java.txt` + `scripts/check-test-integrity.sh` |
| 4 | 长任务交接班 | [04-long-task-handoff](04-long-task-handoff/) | `PROMPT.md`、`progress.md` 模板 |
| 5 | 独立验收者 | [05-separate-validator](05-separate-validator/) | `maker-prompt.md`、`validator-prompt.md` |
| 6 | 定时 / CI 触发 | [06-scheduled-trigger](06-scheduled-trigger/) | `nightly-agent.yml.example` |
| 7 | 预算与迭代上限 | [07-budget-caps](07-budget-caps/) | `loop-with-brakes.sh` |
| 8 | 多 Agent 组队 loop | [08-team-loop](08-team-loop/) | 用 herdr-team-loop-cli 跑本仓库 |

通用约定：

- 每次实验新开分支：`git switch -c exp/02-ralph-<日期>`，跑完不合并，结果写进 PR 或分支里的 `RESULT.md`。
- 「完成」的唯一标准：`./mvnw -B verify` 和 `scripts/check-test-integrity.sh origin/main` 同时通过。
- 记录：迭代轮数、耗时、模型 / 工具及版本、花费（如果工具给出）、人工介入次数。只记真实数字，没有就写「未测」。
