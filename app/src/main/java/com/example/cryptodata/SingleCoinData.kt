package com.example.cryptodata

data class SingleCoinData(
	val data: Datax? = null,
	val status: String? = null
)

data class AllTimeHigh(
	val price: String? = null,
	val timestamp: Int? = null
)

data class Datax(
	val coin: Coin? = null
)

data class LinksItem(
	val name: String? = null,
	val type: String? = null,
	val url: String? = null
)

data class Supply(
	val circulating: String? = null,
	val total: String? = null,
	val max: String? = null,
	val confirmed: Boolean? = null,
	val supplyAt: Int? = null
)

data class Coin(
	val symbol: String? = null,
	val color: String? = null,
	val description: String? = null,
	val uuid: String? = null,
	val supply: Supply? = null,
	val sparkline: List<String?>? = null,
	val jsonMember24hVolume: String? = null,
	val tier: Int? = null,
	val websiteUrl: String? = null,
	val allTimeHigh: AllTimeHigh? = null,
	val coinrankingUrl: String? = null,
	val price: String? = null,
	val lowVolume: Boolean? = null,
	val fullyDilutedMarketCap: String? = null,
	val rank: Int? = null,
	val links: List<LinksItem?>? = null,
	val iconUrl: String? = null,
	val marketCap: String? = null,
	val numberOfMarkets: Int? = null,
	val priceAt: Int? = null,
	val hasContent: Boolean? = null,
	val change: String? = null,
	val btcPrice: String? = null,
	val listedAt: Int? = null,
	val tags: List<String?>? = null,
	val notices: Any? = null,
	val name: String? = null,
	val numberOfExchanges: Int? = null
)

