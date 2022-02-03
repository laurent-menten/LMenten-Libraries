package be.lmenten.utils.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Country
{
	AD( "AD", "AND", 20 ),		// Andorra
	AE( "AE", "ARE", 784 ),		// United Arab Emirates
	AF( "AF", "AFG", 4 ),		// Afghanistan
	AG( "AG", "ATG", 28 ),		// Antigua & Barbuda
	AI( "AI", "AIA", 660 ),		// Anguilla
	AL( "AL", "ALB", 8 ),		// Albania
	AM( "AM", "ARM", 51 ),		// Armenia
	AO( "AO", "AGO", 24 ),		// Angola
	AQ( "AQ", "ATA", 10 ),		// Antarctica
	AR( "AR", "ARG", 32 ),		// Argentina
	AS( "AS", "ASM", 16 ),		// American Samoa
	AT( "AT", "AUT", 40 ),		// Austria
	AU( "AU", "AUS", 36 ),		// Australia
	AW( "AW", "ABW", 533 ),		// Aruba
	AX( "AX", "ALA", 248 ),		// Åland Islands
	AZ( "AZ", "AZE", 31 ),		// Azerbaijan
	BA( "BA", "BIH", 70 ),		// Bosnia & Herzegovina
	BB( "BB", "BRB", 52 ),		// Barbados
	BD( "BD", "BGD", 50 ),		// Bangladesh
	BE( "BE", "BEL", 56 ),		// Belgium
	BF( "BF", "BFA", 854 ),		// Burkina Faso
	BG( "BG", "BGR", 100 ),		// Bulgaria
	BH( "BH", "BHR", 48 ),		// Bahrain
	BI( "BI", "BDI", 108 ),		// Burundi
	BJ( "BJ", "BEN", 204 ),		// Benin
	BL( "BL", "BLM", 652 ),		// St Barthélemy
	BM( "BM", "BMU", 60 ),		// Bermuda
	BN( "BN", "BRN", 96 ),		// Brunei
	BO( "BO", "BOL", 68 ),		// Bolivia
	BQ( "BQ", "BES", 535 ),		// Caribbean Netherlands
	BR( "BR", "BRA", 76 ),		// Brazil
	BS( "BS", "BHS", 44 ),		// Bahamas
	BT( "BT", "BTN", 64 ),		// Bhutan
	BV( "BV", "BVT", 74 ),		// Bouvet Island
	BW( "BW", "BWA", 72 ),		// Botswana
	BY( "BY", "BLR", 112 ),		// Belarus
	BZ( "BZ", "BLZ", 84 ),		// Belize
	CA( "CA", "CAN", 124 ),		// Canada
	CC( "CC", "CCK", 166 ),		// Cocos (Keeling) Islands
	CD( "CD", "COD", 180 ),		// Congo - Kinshasa
	CF( "CF", "CAF", 140 ),		// Central African Republic
	CG( "CG", "COG", 178 ),		// Congo - Brazzaville
	CH( "CH", "CHE", 756 ),		// Switzerland
	CI( "CI", "CIV", 384 ),		// Côte d’Ivoire
	CK( "CK", "COK", 184 ),		// Cook Islands
	CL( "CL", "CHL", 152 ),		// Chile
	CM( "CM", "CMR", 120 ),		// Cameroon
	CN( "CN", "CHN", 156 ),		// China
	CO( "CO", "COL", 170 ),		// Colombia
	CR( "CR", "CRI", 188 ),		// Costa Rica
	CU( "CU", "CUB", 192 ),		// Cuba
	CV( "CV", "CPV", 132 ),		// Cape Verde
	CW( "CW", "CUW", 531 ),		// Curaçao
	CX( "CX", "CXR", 162 ),		// Christmas Island
	CY( "CY", "CYP", 196 ),		// Cyprus
	CZ( "CZ", "CZE", 203 ),		// Czechia
	DE( "DE", "DEU", 276 ),		// Germany
	DJ( "DJ", "DJI", 262 ),		// Djibouti
	DK( "DK", "DNK", 208 ),		// Denmark
	DM( "DM", "DMA", 212 ),		// Dominica
	DO( "DO", "DOM", 214 ),		// Dominican Republic
	DZ( "DZ", "DZA", 12 ),		// Algeria
	EC( "EC", "ECU", 218 ),		// Ecuador
	EE( "EE", "EST", 233 ),		// Estonia
	EG( "EG", "EGY", 818 ),		// Egypt
	EH( "EH", "ESH", 732 ),		// Western Sahara
	ER( "ER", "ERI", 232 ),		// Eritrea
	ES( "ES", "ESP", 724 ),		// Spain
	ET( "ET", "ETH", 231 ),		// Ethiopia
	FI( "FI", "FIN", 246 ),		// Finland
	FJ( "FJ", "FJI", 242 ),		// Fiji
	FK( "FK", "FLK", 238 ),		// Falkland Islands
	FM( "FM", "FSM", 583 ),		// Micronesia
	FO( "FO", "FRO", 234 ),		// Faroe Islands
	FR( "FR", "FRA", 250 ),		// France
	GA( "GA", "GAB", 266 ),		// Gabon
	GB( "GB", "GBR", 826 ),		// United Kingdom
	GD( "GD", "GRD", 308 ),		// Grenada
	GE( "GE", "GEO", 268 ),		// Georgia
	GF( "GF", "GUF", 254 ),		// French Guiana
	GG( "GG", "GGY", 831 ),		// Guernsey
	GH( "GH", "GHA", 288 ),		// Ghana
	GI( "GI", "GIB", 292 ),		// Gibraltar
	GL( "GL", "GRL", 304 ),		// Greenland
	GM( "GM", "GMB", 270 ),		// Gambia
	GN( "GN", "GIN", 324 ),		// Guinea
	GP( "GP", "GLP", 312 ),		// Guadeloupe
	GQ( "GQ", "GNQ", 226 ),		// Equatorial Guinea
	GR( "GR", "GRC", 300 ),		// Greece
	GS( "GS", "SGS", 239 ),		// South Georgia & South Sandwich Islands
	GT( "GT", "GTM", 320 ),		// Guatemala
	GU( "GU", "GUM", 316 ),		// Guam
	GW( "GW", "GNB", 624 ),		// Guinea-Bissau
	GY( "GY", "GUY", 328 ),		// Guyana
	HK( "HK", "HKG", 344 ),		// Hong Kong SAR China
	HM( "HM", "HMD", 334 ),		// Heard & McDonald Islands
	HN( "HN", "HND", 340 ),		// Honduras
	HR( "HR", "HRV", 191 ),		// Croatia
	HT( "HT", "HTI", 332 ),		// Haiti
	HU( "HU", "HUN", 348 ),		// Hungary
	ID( "ID", "IDN", 360 ),		// Indonesia
	IE( "IE", "IRL", 372 ),		// Ireland
	IL( "IL", "ISR", 376 ),		// Israel
	IM( "IM", "IMN", 833 ),		// Isle of Man
	IN( "IN", "IND", 356 ),		// India
	IO( "IO", "IOT", 86 ),		// British Indian Ocean Territory
	IQ( "IQ", "IRQ", 368 ),		// Iraq
	IR( "IR", "IRN", 364 ),		// Iran
	IS( "IS", "ISL", 352 ),		// Iceland
	IT( "IT", "ITA", 380 ),		// Italy
	JE( "JE", "JEY", 832 ),		// Jersey
	JM( "JM", "JAM", 388 ),		// Jamaica
	JO( "JO", "JOR", 400 ),		// Jordan
	JP( "JP", "JPN", 392 ),		// Japan
	KE( "KE", "KEN", 404 ),		// Kenya
	KG( "KG", "KGZ", 417 ),		// Kyrgyzstan
	KH( "KH", "KHM", 116 ),		// Cambodia
	KI( "KI", "KIR", 296 ),		// Kiribati
	KM( "KM", "COM", 174 ),		// Comoros
	KN( "KN", "KNA", 659 ),		// St Kitts & Nevis
	KP( "KP", "PRK", 408 ),		// North Korea
	KR( "KR", "KOR", 410 ),		// South Korea
	KW( "KW", "KWT", 414 ),		// Kuwait
	KY( "KY", "CYM", 136 ),		// Cayman Islands
	KZ( "KZ", "KAZ", 398 ),		// Kazakhstan
	LA( "LA", "LAO", 418 ),		// Laos
	LB( "LB", "LBN", 422 ),		// Lebanon
	LC( "LC", "LCA", 662 ),		// St Lucia
	LI( "LI", "LIE", 438 ),		// Liechtenstein
	LK( "LK", "LKA", 144 ),		// Sri Lanka
	LR( "LR", "LBR", 430 ),		// Liberia
	LS( "LS", "LSO", 426 ),		// Lesotho
	LT( "LT", "LTU", 440 ),		// Lithuania
	LU( "LU", "LUX", 442 ),		// Luxembourg
	LV( "LV", "LVA", 428 ),		// Latvia
	LY( "LY", "LBY", 434 ),		// Libya
	MA( "MA", "MAR", 504 ),		// Morocco
	MC( "MC", "MCO", 492 ),		// Monaco
	MD( "MD", "MDA", 498 ),		// Moldova
	ME( "ME", "MNE", 499 ),		// Montenegro
	MF( "MF", "MAF", 663 ),		// St Martin
	MG( "MG", "MDG", 450 ),		// Madagascar
	MH( "MH", "MHL", 584 ),		// Marshall Islands
	MK( "MK", "MKD", 807 ),		// North Macedonia
	ML( "ML", "MLI", 466 ),		// Mali
	MM( "MM", "MMR", 104 ),		// Myanmar (Burma)
	MN( "MN", "MNG", 496 ),		// Mongolia
	MO( "MO", "MAC", 446 ),		// Macao SAR China
	MP( "MP", "MNP", 580 ),		// Northern Mariana Islands
	MQ( "MQ", "MTQ", 474 ),		// Martinique
	MR( "MR", "MRT", 478 ),		// Mauritania
	MS( "MS", "MSR", 500 ),		// Montserrat
	MT( "MT", "MLT", 470 ),		// Malta
	MU( "MU", "MUS", 480 ),		// Mauritius
	MV( "MV", "MDV", 462 ),		// Maldives
	MW( "MW", "MWI", 454 ),		// Malawi
	MX( "MX", "MEX", 484 ),		// Mexico
	MY( "MY", "MYS", 458 ),		// Malaysia
	MZ( "MZ", "MOZ", 508 ),		// Mozambique
	NA( "NA", "NAM", 516 ),		// Namibia
	NC( "NC", "NCL", 540 ),		// New Caledonia
	NE( "NE", "NER", 562 ),		// Niger
	NF( "NF", "NFK", 574 ),		// Norfolk Island
	NG( "NG", "NGA", 566 ),		// Nigeria
	NI( "NI", "NIC", 558 ),		// Nicaragua
	NL( "NL", "NLD", 528 ),		// Netherlands
	NO( "NO", "NOR", 578 ),		// Norway
	NP( "NP", "NPL", 524 ),		// Nepal
	NR( "NR", "NRU", 520 ),		// Nauru
	NU( "NU", "NIU", 570 ),		// Niue
	NZ( "NZ", "NZL", 554 ),		// New Zealand
	OM( "OM", "OMN", 512 ),		// Oman
	PA( "PA", "PAN", 591 ),		// Panama
	PE( "PE", "PER", 604 ),		// Peru
	PF( "PF", "PYF", 258 ),		// French Polynesia
	PG( "PG", "PNG", 598 ),		// Papua New Guinea
	PH( "PH", "PHL", 608 ),		// Philippines
	PK( "PK", "PAK", 586 ),		// Pakistan
	PL( "PL", "POL", 616 ),		// Poland
	PM( "PM", "SPM", 666 ),		// St Pierre & Miquelon
	PN( "PN", "PCN", 612 ),		// Pitcairn Islands
	PR( "PR", "PRI", 630 ),		// Puerto Rico
	PS( "PS", "PSE", 275 ),		// Palestinian Territories
	PT( "PT", "PRT", 620 ),		// Portugal
	PW( "PW", "PLW", 585 ),		// Palau
	PY( "PY", "PRY", 600 ),		// Paraguay
	QA( "QA", "QAT", 634 ),		// Qatar
	RE( "RE", "REU", 638 ),		// Réunion
	RO( "RO", "ROU", 642 ),		// Romania
	RS( "RS", "SRB", 688 ),		// Serbia
	RU( "RU", "RUS", 643 ),		// Russia
	RW( "RW", "RWA", 646 ),		// Rwanda
	SA( "SA", "SAU", 682 ),		// Saudi Arabia
	SB( "SB", "SLB", 90 ),		// Solomon Islands
	SC( "SC", "SYC", 690 ),		// Seychelles
	SD( "SD", "SDN", 729 ),		// Sudan
	SE( "SE", "SWE", 752 ),		// Sweden
	SG( "SG", "SGP", 702 ),		// Singapore
	SH( "SH", "SHN", 654 ),		// St Helena
	SI( "SI", "SVN", 705 ),		// Slovenia
	SJ( "SJ", "SJM", 744 ),		// Svalbard & Jan Mayen
	SK( "SK", "SVK", 703 ),		// Slovakia
	SL( "SL", "SLE", 694 ),		// Sierra Leone
	SM( "SM", "SMR", 674 ),		// San Marino
	SN( "SN", "SEN", 686 ),		// Senegal
	SO( "SO", "SOM", 706 ),		// Somalia
	SR( "SR", "SUR", 740 ),		// Suriname
	SS( "SS", "SSD", 728 ),		// South Sudan
	ST( "ST", "STP", 678 ),		// São Tomé & Príncipe
	SV( "SV", "SLV", 222 ),		// El Salvador
	SX( "SX", "SXM", 534 ),		// Sint Maarten
	SY( "SY", "SYR", 760 ),		// Syria
	SZ( "SZ", "SWZ", 748 ),		// Eswatini
	TC( "TC", "TCA", 796 ),		// Turks & Caicos Islands
	TD( "TD", "TCD", 148 ),		// Chad
	TF( "TF", "ATF", 260 ),		// French Southern Territories
	TG( "TG", "TGO", 768 ),		// Togo
	TH( "TH", "THA", 764 ),		// Thailand
	TJ( "TJ", "TJK", 762 ),		// Tajikistan
	TK( "TK", "TKL", 772 ),		// Tokelau
	TL( "TL", "TLS", 626 ),		// Timor-Leste
	TM( "TM", "TKM", 795 ),		// Turkmenistan
	TN( "TN", "TUN", 788 ),		// Tunisia
	TO( "TO", "TON", 776 ),		// Tonga
	TR( "TR", "TUR", 792 ),		// Turkey
	TT( "TT", "TTO", 780 ),		// Trinidad & Tobago
	TV( "TV", "TUV", 798 ),		// Tuvalu
	TW( "TW", "TWN", 158 ),		// Taiwan
	TZ( "TZ", "TZA", 834 ),		// Tanzania
	UA( "UA", "UKR", 804 ),		// Ukraine
	UG( "UG", "UGA", 800 ),		// Uganda
	UM( "UM", "UMI", 581 ),		// US Outlying Islands
	US( "US", "USA", 840 ),		// United States
	UY( "UY", "URY", 858 ),		// Uruguay
	UZ( "UZ", "UZB", 860 ),		// Uzbekistan
	VA( "VA", "VAT", 336 ),		// Vatican City
	VC( "VC", "VCT", 670 ),		// St Vincent & the Grenadines
	VE( "VE", "VEN", 862 ),		// Venezuela
	VG( "VG", "VGB", 92 ),		// British Virgin Islands
	VI( "VI", "VIR", 850 ),		// US Virgin Islands
	VN( "VN", "VNM", 704 ),		// Vietnam
	VU( "VU", "VUT", 548 ),		// Vanuatu
	WF( "WF", "WLF", 876 ),		// Wallis & Futuna
	WS( "WS", "WSM", 882 ),		// Samoa
	YE( "YE", "YEM", 887 ),		// Yemen
	YT( "YT", "MYT", 175 ),		// Mayotte
	ZA( "ZA", "ZAF", 710 ),		// South Africa
	ZM( "ZM", "ZMB", 894 ),		// Zambia
	ZW( "ZW", "ZWE", 716 )		// Zimbabwe
	;

	// ------------------------------------------------------------------------

	private final String code2;
	private final String code3;
	private final int numeric;

	// ========================================================================
	// = Constructor ==========================================================
	// ========================================================================

	Country( String code2, String code3, int numeric )
	{
		this.code2 = code2;
		this.code3 = code3;
		this.numeric = numeric;
	}

	// ========================================================================
	// =
	// ========================================================================

	public String getCode2()
	{
		return code2;
	}

	public String getCode3()
	{
		return code3;
	}

	public int getNumeric()
	{
		return numeric;
	}

	public String getName()
	{
		return RESOURCE.getString( name() );
	}

	// ========================================================================
	// =
	// ========================================================================

	@Override
	public String toString()
	{
		return "Country{" +
				"code2='" + code2 + '\'' +
				", code3='" + code3 + '\'' +
				", numeric=" + numeric +
				", name='" + getName() + '\'' +
				'}';
	}

	// ========================================================================
	// = Utilities ============================================================
	// ========================================================================

	private static final ResourceBundle RESOURCE
		= ResourceBundle.getBundle( Country.class.getName() );

	// ========================================================================
	// = GENERATOR ============================================================
	// ========================================================================

	public static void main( String[] args )
	{
		String[] countryCodes = Locale.getISOCountries();

//		Locale.setDefault( new Locale( "fr" ) );

		for (String cc : countryCodes)
		{
			var l = new Locale( "", cc );
			var l2 = Country.valueOf( cc.toUpperCase() );

			StringBuilder s = new StringBuilder();

/*			s.append( cc.toUpperCase() )
				.append( "( " )
				.append( "\"" )
				.append( cc.toUpperCase() )
				.append( "\", \"" )
				.append( l.getISO3Country() )
				.append( "\", " )
				.append( l2.getNumeric() )
				.append( " ),\t\t" )
				.append( "// " )
				.append( l.getDisplayCountry() )
				;
*/

/*
			s.append( cc.toUpperCase() )
				.append( "=" )
				.append( l.getDisplayCountry() )
				;
*/

			System.out.println( s.toString() );
		}
	}
}
