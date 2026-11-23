# fix_plan：优惠券有效期 + 品类券

- [ ] 1. `Coupon` 增加有效期 `validFrom` / `validTo`（`LocalDate`，可为空表示不限），过期券在 quote 时返回 422
- [ ] 2. 注入 `java.time.Clock` 让「今天」可测试，测试里用固定时钟
- [ ] 3. `CartItem` 增加 `category` 字段（如 BOOK、STATIONERY），旧请求不传时默认 `GENERAL`
- [ ] 4. 新增品类券 `CategoryCoupon`：只对指定品类的商品小计生效，例如「图书满 100 减 15」
- [ ] 5. `GET /api/coupons` 返回有效期和适用品类；补 MockMvc 测试
