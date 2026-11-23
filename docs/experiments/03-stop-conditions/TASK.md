实现 `dev.agentloop.lab.checkout.DiscountAllocator`：

- `List<BigDecimal> allocate(List<CartItem> items, BigDecimal totalDiscount)`
- 按每行金额占小计的比例分摊折扣，scale 2，HALF_UP。
- 各行分摊之和必须**严格等于** totalDiscount；尾差补给金额最大的那一行（金额相同取靠前的）。
- 任何一行的分摊都不能超过该行金额。

完成标准见 README。不许修改 AllocationSpecTest。
