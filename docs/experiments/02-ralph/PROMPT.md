你在一个 Spring Boot 3 / Java 17 仓库里工作，先读 AGENTS.md。

每一轮只做一件事：
1. 读 fix_plan.md，选出第一个未勾选的 `- [ ]` 项。
2. 先写或补测试，再实现，直到 `./mvnw -B verify` 通过。
3. 把这一项改成 `- [x]`，在后面用一句话写你做了什么。
4. git commit，提交信息写这一项的标题。
5. 如果所有项都已勾选并且 verify 通过，在 fix_plan.md 末尾写一行 `ALL_DONE` 后结束。

禁止：删除或跳过测试、修改 scripts/ 和 .github/、降低覆盖率门槛。
