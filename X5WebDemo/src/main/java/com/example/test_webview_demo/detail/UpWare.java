package com.example.test_webview_demo.detail;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class UpWare {
  private Long id;   //商品ID
  private String sn; //编号
  private Integer team_buying_people;//团购人数
  private Long category_id;//商品分类
  private Integer productType;//商品类型
  private Integer isPresale;//是否预售
  private String selectPresaleTime;//预售时间
  private String shippingPromise;//发货时间
  private Integer groundless_7day;//7天无理由退货
  private String product_title;//商品标题
  private String market_price;//市场价格
  private String areas;//配送地区
  private String product_detail;//商品详情
  private String large;//商品大图
  private String thumbnail;//略缩图
  private String thumbnail_hd;//高清略缩图
  private String detailPhoto;//详情图片
  private String carouselPhoto;//轮播图
  private String skuExp;//获取库存信息
  private Double single_clusterPrice;//单品团价
  private Double single_price;//单品单价
  private Integer count_sku;//设置总库存
  private Integer checkStatus;
  private String attributes;//属性值
  private String deleteSkuIds;//编辑时 删除的Sku的ids
  private Long shopId;//店铺ID

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public Integer getTeam_buying_people() {
    return team_buying_people;
  }

  public void setTeam_buying_people(Integer team_buying_people) {
    this.team_buying_people = team_buying_people;
  }

  public Long getCategory_id() {
    return category_id;
  }

  public void setCategory_id(Long category_id) {
    this.category_id = category_id;
  }

  public Integer getProductType() {
    return productType;
  }

  public void setProductType(Integer productType) {
    this.productType = productType;
  }

  public Integer getIsPresale() {
    return isPresale;
  }

  public void setIsPresale(Integer isPresale) {
    this.isPresale = isPresale;
  }

  public String getSelectPresaleTime() {
    return selectPresaleTime;
  }

  public void setSelectPresaleTime(String selectPresaleTime) {
    this.selectPresaleTime = selectPresaleTime;
  }

  public String getShippingPromise() {
    return shippingPromise;
  }

  public void setShippingPromise(String shippingPromise) {
    this.shippingPromise = shippingPromise;
  }

  public Integer getGroundless_7day() {
    return groundless_7day;
  }

  public void setGroundless_7day(Integer groundless_7day) {
    this.groundless_7day = groundless_7day;
  }

  public String getProduct_title() {
    return product_title;
  }

  public void setProduct_title(String product_title) {
    this.product_title = product_title;
  }

  public String getMarket_price() {
    return market_price;
  }

  public void setMarket_price(String market_price) {
    this.market_price = market_price;
  }

  public String getAreas() {
    return areas;
  }

  public void setAreas(String areas) {
    this.areas = areas;
  }

  public String getProduct_detail() {
    return product_detail;
  }

  public void setProduct_detail(String product_detail) {
    this.product_detail = product_detail;
  }

  public String getLarge() {
    return large;
  }

  public void setLarge(String large) {
    this.large = large;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getThumbnail_hd() {
    return thumbnail_hd;
  }

  public void setThumbnail_hd(String thumbnail_hd) {
    this.thumbnail_hd = thumbnail_hd;
  }

  public String getDetailPhoto() {
    return detailPhoto;
  }

  public void setDetailPhoto(String detailPhoto) {
    this.detailPhoto = detailPhoto;
  }

  public String getCarouselPhoto() {
    return carouselPhoto;
  }

  public void setCarouselPhoto(String carouselPhoto) {
    this.carouselPhoto = carouselPhoto;
  }

  public String getSkuExp() {
    return skuExp;
  }

  public void setSkuExp(String skuExp) {
    this.skuExp = skuExp;
  }

  public Double getSingle_clusterPrice() {
    return single_clusterPrice;
  }

  public void setSingle_clusterPrice(Double single_clusterPrice) {
    this.single_clusterPrice = single_clusterPrice;
  }

  public Double getSingle_price() {
    return single_price;
  }

  public void setSingle_price(Double single_price) {
    this.single_price = single_price;
  }

  public Integer getCount_sku() {
    return count_sku;
  }

  public void setCount_sku(Integer count_sku) {
    this.count_sku = count_sku;
  }

  public Integer getCheckStatus() {
    return checkStatus;
  }

  public void setCheckStatus(Integer checkStatus) {
    this.checkStatus = checkStatus;
  }

  public String getAttributes() {
    return attributes;
  }

  public void setAttributes(String attributes) {
    this.attributes = attributes;
  }

  public String getDeleteSkuIds() {
    return deleteSkuIds;
  }

  public void setDeleteSkuIds(String deleteSkuIds) {
    this.deleteSkuIds = deleteSkuIds;
  }

  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }
}
