# 05 · 独立验收者：别让学生自己改卷

**目的**：同一个 Agent 写代码再自己说「完成」，和另起一个只读的验收 Agent 按规格逐条检查，两者结论有多大差别。

**任务**：复用 03 的分摊任务，或 02 的 fix_plan 里任意一项。

**流程**

1. Maker：用 `maker-prompt.md` 完成任务并提交。
2. 机械检查：`./mvnw -B verify && scripts/check-test-integrity.sh <基线>`。
3. Validator：**新会话、只读权限**，用 `validator-prompt.md`，输入 = 规格 + `git diff <基线>`，输出 JSON 结论。
4. 结论是 FAIL 就把 `reasons` 原样交回 Maker，最多 3 轮。

**观察点**：Maker 自报「完成」时，Validator 判 FAIL 的比例；Validator 找到的问题有多少是守卫脚本抓不到的（比如断言被改弱）。
