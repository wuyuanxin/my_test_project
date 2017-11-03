package com.my.okhttp;

public class UpdateStock {

	private String  assetCode;
	private String  barCode;
	private Integer goodsStock;
	
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public Integer getGoodsStock() {
		return goodsStock;
	}
	public void setGoodsStock(Integer goodsStock) {
		this.goodsStock = goodsStock;
	}

	public UpdateStock(String assetCode, String barCode, Integer goodsStock) {
		this.assetCode = assetCode;
		this.barCode = barCode;
		this.goodsStock = goodsStock;
	}
}
