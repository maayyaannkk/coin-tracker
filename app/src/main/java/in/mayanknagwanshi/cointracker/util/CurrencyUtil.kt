package `in`.mayanknagwanshi.cointracker.util

data class FiatCurrencyData(val abbr: String, val name: String, val symbol: String)

val supportedFiatCurrencies: List<FiatCurrencyData> =
    listOf(
        FiatCurrencyData("USD", "US Dollar", "$"),
        FiatCurrencyData("AED", "United Arab Emirates Dirham", "د.إ"),
        FiatCurrencyData("ARS", "Argentine Peso", "$"),
        FiatCurrencyData("AUD", "Australian Dollar", "$"),
        FiatCurrencyData("BDT", "Bangladeshi Taka", "৳"),
        FiatCurrencyData("BHD", "Bahraini Dinar", ".د.ب"),
        FiatCurrencyData("BMD", "Bermudian Dollar", "$"),
        FiatCurrencyData("BRL", "Brazilian Real", "R$"),
        FiatCurrencyData("CAD", "Canadian Dollar", "$"),
        FiatCurrencyData("CHF", "Swiss Franc", "Fr."),
        FiatCurrencyData("CLP", "Chilean Peso", "$"),
        FiatCurrencyData("CNY", "Chinese Yuan Renminbi", "¥"),
        FiatCurrencyData("CZK", "Czech Koruna", "Kč"),
        FiatCurrencyData("DKK", "Danish Krone", "kr."),
        FiatCurrencyData("EUR", "Euro", "€"),
        FiatCurrencyData("GBP", "British Pound Sterling", "£"),
        FiatCurrencyData("HKD", "Hong Kong Dollar", "HK$"),
        FiatCurrencyData("HUF", "Hungarian Forint", "Ft"),
        FiatCurrencyData("IDR", "Indonesian Rupiah", "Rp"),
        FiatCurrencyData("ILS", "Israeli New Sheqel", "₪"),
        FiatCurrencyData("INR", "Indian Rupee", "₹"),
        FiatCurrencyData("JPY", "Japanese Yen", "¥"),
        FiatCurrencyData("KRW", "South Korean Won", "₩"),
        FiatCurrencyData("KWD", "Kuwaiti Dinar", "د.ك"),
        FiatCurrencyData("LKR", "Sri Lankan Rupee", "රු"),
        FiatCurrencyData("MMK", "Myanmar Kyat", "Ks"),
        FiatCurrencyData("MXN", "Mexican Peso", "$"),
        FiatCurrencyData("MYR", "Malaysian Ringgit", "RM"),
        FiatCurrencyData("NGN", "Nigerian Naira", "₦"),
        FiatCurrencyData("NOK", "Norwegian Krone", "kr"),
        FiatCurrencyData("NZD", "New Zealand Dollar", "$"),
        FiatCurrencyData("PHP", "Philippine Peso", "₱"),
        FiatCurrencyData("PKR", "Pakistani Rupee", "₨"),
        FiatCurrencyData("PLN", "Polish Złoty", "zł"),
        FiatCurrencyData("RUB", "Russian Ruble", "₽"),
        FiatCurrencyData("SAR", "Saudi Riyal", "﷼"),
        FiatCurrencyData("SEK", "Swedish Krona", "kr"),
        FiatCurrencyData("SGD", "Singapore Dollar", "$"),
        FiatCurrencyData("THB", "Thai Baht", "฿"),
        FiatCurrencyData("TRY", "Turkish Lira", "₺"),
        FiatCurrencyData("TWD", "New Taiwan Dollar", "NT$"),
        FiatCurrencyData("UAH", "Ukrainian Hryvnia", "₴"),
        FiatCurrencyData("VEF", "Venezuelan Bolívar", "Bs."),
        FiatCurrencyData("VND", "Vietnamese Dong", "₫"),
        FiatCurrencyData("ZAR", "South African Rand", "R")
    )

val currencyNameList: List<String> = supportedFiatCurrencies.map { "${it.abbr}(${it.symbol})" }
