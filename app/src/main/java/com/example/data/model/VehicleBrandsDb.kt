package com.example.data.model

data class VehicleSpec(
    val type: String, // "CAR", "MOTO", "TRUCK"
    val brand: String,
    val model: String,
    val engine: String,
    val oilSpec: String,
    val oilCapacity: String,
    val tirePressure: String,
    val fuelTank: String,
    val fuelType: String,
    val sparkPlug: String,
    val brakeFluid: String,
    val coolant: String,
    val intervalKm: Double
)

object VehicleBrandsDb {

    val specsCatalog = listOf(
        // ==========================================
        // CARROS - CHEVROLET
        // ==========================================
        VehicleSpec("CAR", "Chevrolet", "Onix 1.0 Flex", "1.0 12V 3 Cils (82cv)", "0W20 100% Sintético API SP (Dexos 1 Gen 3)", "3.5 Litros", "35 PSI Diant. / 35 PSI Tras.", "44 Litros", "Flex", "NGK ILZKR7B11 (Iridium)", "DOT 4 LV", "Orgânico Rosa 50/50 (4.5L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Onix Plus 1.0 Turbo", "1.0 12V Turbo (116cv)", "0W20 100% Sintético API SP (Dexos 1 Gen 3)", "3.75 Litros", "35 PSI Diant. / 35 PSI Tras.", "44 Litros", "Flex", "NGK ILZKR7B11", "DOT 4 LV", "Orgânico Rosa 50/50 (4.8L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Tracker 1.0 / 1.2 Turbo", "1.0 / 1.2 Turbo Flex (116cv/133cv)", "0W20 100% Sintético (Dexos 1 Gen 3)", "4.0 Litros", "35 PSI Diant. / 35 PSI Tras.", "44 Litros", "Flex", "NGK Iridium", "DOT 4 LV", "Orgânico Rosa 50/50 (5.0L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Cruze 1.4 Turbo", "1.4 16V Ecotec Turbo (153cv)", "5W30 100% Sintético (Dexos 1 Gen 2)", "4.0 Litros", "32 PSI Diant. / 32 PSI Tras.", "52 Litros", "Flex", "NGK Iridium", "DOT 4", "Orgânico Laranja (5.5L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Celta 1.0 VHC-E", "1.0 8V VHC-E (78cv)", "5W30 Semissintético ou Sintético API SL/SM", "3.5 Litros", "28 PSI Diant. / 28 PSI Tras.", "54 Litros", "Flex", "NGK BPR6EY", "DOT 4", "Etilenoglicol 50/50 (5.0L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Corsa 1.4 / 1.8", "1.4 / 1.8 8V Econoflex", "5W30 ou 15W40 API SL/SN", "3.5 Litros", "30 PSI Diant. / 28 PSI Tras.", "46 Litros", "Flex", "NGK BPR6EY", "DOT 4", "Orgânico 50/50", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Prisma 1.0 / 1.4 Flex", "1.0 / 1.4 SPE/4 Flex", "5W30 ou 0W20 Sintético Dexos 1", "3.5 Litros", "32 PSI Diant. / 32 PSI Tras.", "54 Litros", "Flex", "NGK BPR6EY-D", "DOT 4", "Orgânico Rosa (5.0L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Spin 1.8 Flex", "1.8 8V SPE/4 ECO (111cv)", "0W20 ou 5W30 Sintético", "3.5 Litros", "35 PSI Diant. / 35 PSI Tras.", "53 Litros", "Flex", "NGK BPR6EY-D", "DOT 4", "Orgânico Rosa", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Montana 1.2 Turbo", "1.2 12V Turbo Flex (133cv)", "0W20 100% Sintético (Dexos 1 Gen 3)", "4.0 Litros", "35 PSI Diant. / 38 PSI Tras.", "44 Litros", "Flex", "NGK Iridium", "DOT 4 LV", "Orgânico Rosa (5.2L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "S10 2.8 Turbo Diesel", "2.8 Duramax Turbo Diesel (200cv)", "5W30 100% Sintético (Dexos 2)", "5.6 Litros", "35 PSI Diant. / 38 PSI Tras.", "76 Litros", "Diesel", "Velas Aquecedoras Diesel", "DOT 4", "Orgânico Rosa (9.0L)", 10000.0),
        VehicleSpec("CAR", "Chevrolet", "Astra 2.0 8V", "2.0 8V Família II (140cv)", "5W30 ou 15W40 Semissintético", "4.25 Litros", "30 PSI Diant. / 30 PSI Tras.", "52 Litros", "Flex", "NGK BPR6EY", "DOT 4", "Orgânico (6.5L)", 10000.0),

        // ==========================================
        // CARROS - FIAT
        // ==========================================
        VehicleSpec("CAR", "Fiat", "Argo 1.0 Firefly", "1.0 6V 3 Cils Firefly (77cv)", "0W20 100% Sintético Selènia Forward", "2.7 Litros", "32 PSI Diant. / 32 PSI Tras.", "48 Litros", "Flex", "NGK LZKR6AI-10G (Iridium)", "DOT 4", "Petronas Paraflu UP (4.8L)", 10000.0),
        VehicleSpec("CAR", "Fiat", "Argo 1.3 Firefly", "1.3 8V 4 Cils Firefly (109cv)", "0W20 100% Sintético Selènia Forward", "3.2 Litros", "32 PSI Diant. / 32 PSI Tras.", "48 Litros", "Flex", "NGK Iridium", "DOT 4", "Petronas Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Mobi 1.0 Fire", "1.0 8V Fire EVO (74cv)", "5W30 ou 15W40 Sintético Selènia K Forward", "2.7 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "NGK ZKR7A-10", "DOT 4", "Petronas Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Uno 1.0 Fire / EVO", "1.0 8V Fire EVO (75cv)", "5W30 Sintético Selènia Perform", "2.7 Litros", "30 PSI Diant. / 28 PSI Tras.", "48 Litros", "Flex", "NGK ZKR7A-10", "DOT 4", "Paraflu UP Rosa", 10000.0),
        VehicleSpec("CAR", "Fiat", "Palio 1.0 / 1.4 Fire", "1.0 / 1.4 8V Fire Flex", "15W40 Semissintético ou 5W30", "2.7 Litros", "29 PSI Diant. / 29 PSI Tras.", "48 Litros", "Flex", "NGK BKR6E / ZKR7A", "DOT 4", "Paraflu 50/50", 10000.0),
        VehicleSpec("CAR", "Fiat", "Strada 1.3 Firefly", "1.3 8V Firefly Flex (107cv)", "0W20 100% Sintético Selènia", "3.2 Litros", "32 PSI Diant. / 36 PSI Tras.", "55 Litros", "Flex", "NGK Iridium", "DOT 4", "Petronas Paraflu UP (5.0L)", 10000.0),
        VehicleSpec("CAR", "Fiat", "Strada 1.4 Fire", "1.4 8V Fire EVO (86cv)", "5W30 ou 15W40 Semissintético", "2.7 Litros", "32 PSI Diant. / 38 PSI Tras.", "55 Litros", "Flex", "NGK ZKR7A-10", "DOT 4", "Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Pulse 1.0 Turbo 200", "1.0 12V Turbo 3 Cils (130cv)", "0W20 100% Sintético Selènia", "3.3 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "NGK Iridium", "DOT 4", "Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Fastback 1.0 / 1.3 Turbo", "1.0 Turbo (130cv) / 1.3 Turbo 270 (185cv)", "0W20 100% Sintético Selènia", "4.8 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "NGK Iridium", "DOT 4", "Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Toro 1.3 Turbo 270", "1.3 16V Turbo Flex GSE (185cv)", "0W20 ou 0W30 Sintético Selènia", "4.8 Litros", "35 PSI Diant. / 35 PSI Tras.", "55 Litros", "Flex", "NGK Iridium", "DOT 4", "Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Toro 2.0 Turbo Diesel", "2.0 16V Multijet Diesel (170cv)", "5W30 Sintético ACEA C2", "4.8 Litros", "35 PSI Diant. / 35 PSI Tras.", "60 Litros", "Diesel", "Aquecedoras Diesel", "DOT 4", "Paraflu UP (6.5L)", 10000.0),
        VehicleSpec("CAR", "Fiat", "Cronos 1.3 Drive", "1.3 8V Firefly (109cv)", "0W20 Sintético Selènia", "3.2 Litros", "32 PSI Diant. / 32 PSI Tras.", "48 Litros", "Flex", "NGK Iridium", "DOT 4", "Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Fiat", "Fiorino 1.4 EVO", "1.4 8V Fire EVO (86cv)", "5W30 Sintético Selènia", "2.8 Litros", "32 PSI Diant. / 38 PSI Tras.", "55 Litros", "Flex", "NGK ZKR7A", "DOT 4", "Paraflu UP", 10000.0),

        // ==========================================
        // CARROS - VOLKSWAGEN
        // ==========================================
        VehicleSpec("CAR", "Volkswagen", "Gol 1.0 / 1.6 MSI", "1.0 12V 3 Cils / 1.6 8V EA111", "5W40 100% Sintético VW 508.88 / 502.00", "3.3 Litros", "30 PSI Diant. / 30 PSI Tras.", "55 Litros", "Flex", "NGK PZFR6R (Laser Platinum)", "DOT 4", "VW G12 / G13 Rosa (5.5L)", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Polo 1.0 TSI / Track", "1.0 12V Turbo TSI (116cv) / 1.0 MPI", "5W40 ou 0W20 VW 508.88 / VW 508.00", "3.8 Litros", "32 PSI Diant. / 32 PSI Tras.", "52 Litros", "Flex", "NGK Iridium", "DOT 4", "VW G12 Evo (5.0L)", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Virtus 1.0 TSI / 200 TSI", "1.0 12V Turbo TSI (128cv)", "5W40 ou 0W20 Maxi Performance VW", "3.8 Litros", "32 PSI Diant. / 32 PSI Tras.", "52 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "VW G12 Evo", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "T-Cross 1.0 / 1.4 TSI", "1.0 TSI (128cv) / 1.4 TSI 250 (150cv)", "5W40 ou 0W20 Maxi Performance", "4.0 Litros", "32 PSI Diant. / 32 PSI Tras.", "52 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "VW G12 Evo", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Nivus 1.0 TSI", "1.0 12V 200 TSI Flex (128cv)", "5W40 ou 0W20 Maxi Performance VW", "3.8 Litros", "32 PSI Diant. / 32 PSI Tras.", "52 Litros", "Flex", "NGK Iridium", "DOT 4", "VW G12 Evo", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Fox 1.6 8V", "1.6 8V Total Flex EA111 (104cv)", "5W40 100% Sintético VW 502.00", "3.5 Litros", "30 PSI Diant. / 28 PSI Tras.", "50 Litros", "Flex", "NGK BKR6E", "DOT 4", "VW G12 Rosa", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Voyage 1.0 / 1.6", "1.0 12V / 1.6 8V MSI Flex", "5W40 100% Sintético VW 508.88", "3.5 Litros", "30 PSI Diant. / 30 PSI Tras.", "55 Litros", "Flex", "NGK PZFR6R", "DOT 4", "VW G12 Rosa", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Saveiro 1.6 MSI", "1.6 16V MSI Flex (120cv)", "5W40 100% Sintético VW 508.88", "3.8 Litros", "32 PSI Diant. / 38 PSI Tras.", "55 Litros", "Flex", "NGK Iridium", "DOT 4", "VW G12 Rosa", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Jetta 1.4 / 2.0 TSI", "1.4 TSI (150cv) / 2.0 TSI GLI (230cv)", "5W40 100% Sintético VW 502.00", "4.5 Litros", "35 PSI Diant. / 35 PSI Tras.", "50 Litros", "Gasolina", "NGK Laser Platinum / Iridium", "DOT 4", "VW G13 (6.0L)", 10000.0),
        VehicleSpec("CAR", "Volkswagen", "Amarok 2.0 / 3.0 V6 TDI", "3.0 V6 Turbo Diesel (258cv)", "5W30 100% Sintético VW 507.00 (Low SAPS)", "8.0 Litros", "35 PSI Diant. / 38 PSI Tras.", "80 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "VW G12 Evo (10L)", 10000.0),

        // ==========================================
        // CARROS - TOYOTA
        // ==========================================
        VehicleSpec("CAR", "Toyota", "Corolla 2.0 Dynamic Force", "2.0 16V Dual VVT-iE (177cv)", "0W20 100% Sintético Toyota Genuine", "4.2 Litros", "32 PSI Diant. / 32 PSI Tras.", "50 Litros", "Flex", "Denso SC20HR11 (Iridium)", "DOT 4", "Toyota Super Long Life Pink (6.0L)", 10000.0),
        VehicleSpec("CAR", "Toyota", "Corolla Cross 2.0 Flex / Hybrid", "2.0 Flex (177cv) / 1.8 Híbrido", "0W20 100% Sintético Toyota", "4.2 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "Denso Iridium", "DOT 4", "Toyota Pink Coolant", 10000.0),
        VehicleSpec("CAR", "Toyota", "Hilux 2.8 Turbo Diesel", "2.8 16V D-4D Diesel (204cv)", "5W30 Sintético Toyota Genuine API CJ-4", "7.5 Litros", "32 PSI Diant. / 36 PSI Tras.", "80 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "Toyota Super Long Life (9.0L)", 10000.0),
        VehicleSpec("CAR", "Toyota", "Yaris 1.3 / 1.5 Dual VVT-i", "1.5 16V Flex Dual VVT-i (110cv)", "0W20 100% Sintético Toyota Genuine", "3.7 Litros", "32 PSI Diant. / 30 PSI Tras.", "45 Litros", "Flex", "Denso Iridium", "DOT 4", "Toyota Pink Coolant", 10000.0),
        VehicleSpec("CAR", "Toyota", "Etios 1.3 / 1.5", "1.3 / 1.5 16V Dual VVT-i", "0W20 ou 5W30 Sintético Toyota", "3.5 Litros", "32 PSI Diant. / 30 PSI Tras.", "45 Litros", "Flex", "Denso Iridium", "DOT 4", "Toyota Pink Coolant", 10000.0),
        VehicleSpec("CAR", "Toyota", "SW4 2.8 Diesel 4x4", "2.8 Turbo Diesel D-4D (204cv)", "5W30 100% Sintético Toyota Diesel", "7.5 Litros", "32 PSI Diant. / 36 PSI Tras.", "80 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "Toyota Pink Coolant", 10000.0),

        // ==========================================
        // CARROS - HYUNDAI
        // ==========================================
        VehicleSpec("CAR", "Hyundai", "HB20 1.0 Kappa Flex", "1.0 12V 3 Cils Kappa (80cv)", "5W30 100% Sintético Shell Helix Ultra", "2.9 Litros", "33 PSI Diant. / 33 PSI Tras.", "50 Litros", "Flex", "NGK LZKR6B-10E", "DOT 4", "Etilenoglicol Verde/Azul (4.5L)", 10000.0),
        VehicleSpec("CAR", "Hyundai", "HB20 1.0 TGDI Turbo", "1.0 12V Turbo GDI Flex (120cv)", "0W20 ou 5W30 Sintético API SP / SN Plus", "3.6 Litros", "33 PSI Diant. / 33 PSI Tras.", "50 Litros", "Flex", "NGK SILZKR7B11 (Iridium)", "DOT 4", "Orgânico Long Life", 10000.0),
        VehicleSpec("CAR", "Hyundai", "HB20S 1.0 Flex / Turbo", "1.0 Flex / 1.0 TGDI Turbo", "5W30 ou 0W20 Sintético", "3.6 Litros", "33 PSI Diant. / 33 PSI Tras.", "50 Litros", "Flex", "NGK Iridium", "DOT 4", "Orgânico Long Life", 10000.0),
        VehicleSpec("CAR", "Hyundai", "Creta 1.0 Turbo TGDI", "1.0 12V Turbo GDI (120cv)", "0W20 Sintético API SP / ILSAC GF-6", "3.6 Litros", "33 PSI Diant. / 33 PSI Tras.", "50 Litros", "Flex", "NGK Iridium", "DOT 4", "Orgânico Long Life (5.5L)", 10000.0),
        VehicleSpec("CAR", "Hyundai", "Creta 2.0 Smartstream", "2.0 16V Smartstream Flex (167cv)", "5W30 100% Sintético API SP", "4.0 Litros", "33 PSI Diant. / 33 PSI Tras.", "55 Litros", "Flex", "NGK Iridium", "DOT 4", "Orgânico Long Life", 10000.0),
        VehicleSpec("CAR", "Hyundai", "Tucson 2.0 16V", "2.0 16V DOHC Flex (146cv)", "5W30 ou 10W40 Semissintético", "4.0 Litros", "32 PSI Diant. / 32 PSI Tras.", "65 Litros", "Flex", "NGK BKR5E-11", "DOT 4", "Orgânico Verde (6.0L)", 10000.0),
        VehicleSpec("CAR", "Hyundai", "i30 2.0 / 1.6", "2.0 16V Beta (145cv) / 1.6 16V", "5W30 Sintético API SM/SN", "4.0 Litros", "32 PSI Diant. / 32 PSI Tras.", "53 Litros", "Gasolina", "NGK BKR5E-11", "DOT 4", "Orgânico 50/50", 10000.0),

        // ==========================================
        // CARROS - HONDA
        // ==========================================
        VehicleSpec("CAR", "Honda", "Civic 2.0 i-VTEC (G10)", "2.0 16V i-VTEC Flex (155cv)", "0W20 100% Sintético Honda Genuine Pro", "3.7 Litros", "32 PSI Diant. / 32 PSI Tras.", "56 Litros", "Flex", "NGK DILKAR7H11GS (Laser Iridium)", "DOT 4", "Honda Type 2 Azul (5.2L)", 10000.0),
        VehicleSpec("CAR", "Honda", "Civic 1.8 / 2.0 (G9/G8)", "1.8 / 2.0 16V i-VTEC Flex", "0W20 ou 10W30 Honda Genuine", "3.7 Litros", "32 PSI Diant. / 30 PSI Tras.", "50 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Honda Type 2 Azul", 10000.0),
        VehicleSpec("CAR", "Honda", "HR-V 1.5 i-VTEC / Turbo", "1.5 16V DI i-VTEC (126cv / 177cv Turbo)", "0W20 100% Sintético Honda Genuine", "3.5 Litros", "32 PSI Diant. / 30 PSI Tras.", "50 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Honda Type 2 Azul (4.8L)", 10000.0),
        VehicleSpec("CAR", "Honda", "City 1.5 i-VTEC Flex", "1.5 16V DOHC i-VTEC (126cv)", "0W20 100% Sintético Honda Genuine", "3.3 Litros", "32 PSI Diant. / 30 PSI Tras.", "44 Litros", "Flex", "NGK Iridium", "DOT 4", "Honda Type 2 Azul", 10000.0),
        VehicleSpec("CAR", "Honda", "Fit 1.5 16V i-VTEC", "1.5 16V i-VTEC Flex (116cv)", "0W20 100% Sintético Honda Genuine", "3.4 Litros", "32 PSI Diant. / 30 PSI Tras.", "46 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Honda Type 2 Azul", 10000.0),
        VehicleSpec("CAR", "Honda", "WR-V 1.5 Flex", "1.5 16V i-VTEC Flex (116cv)", "0W20 100% Sintético Honda Genuine", "3.4 Litros", "32 PSI Diant. / 30 PSI Tras.", "45 Litros", "Flex", "NGK Iridium", "DOT 4", "Honda Type 2 Azul", 10000.0),

        // ==========================================
        // CARROS - RENAULT
        // ==========================================
        VehicleSpec("CAR", "Renault", "Kwid 1.0 SCe", "1.0 12V 3 Cils SCe (71cv)", "0W20 ou 5W30 Sintético Elf Evolution", "3.0 Litros", "32 PSI Diant. / 32 PSI Tras.", "38 Litros", "Flex", "NGK Iridium", "DOT 4", "Glaceol RX Type D Amarelo", 10000.0),
        VehicleSpec("CAR", "Renault", "Sandero 1.0 / 1.6 SCe", "1.0 SCe 12V / 1.6 SCe 16V", "5W30 ou 5W40 Sintético Elf", "3.8 Litros", "32 PSI Diant. / 29 PSI Tras.", "50 Litros", "Flex", "NGK LZKAR7A", "DOT 4", "Glaceol RX Type D", 10000.0),
        VehicleSpec("CAR", "Renault", "Duster 1.6 / 1.3 TCe Turbo", "1.3 16V Turbo TCe Flex (170cv)", "5W30 ou 0W20 Sintético RN17", "4.5 Litros", "32 PSI Diant. / 32 PSI Tras.", "50 Litros", "Flex", "NGK Iridium", "DOT 4", "Glaceol RX Amarelo (5.5L)", 10000.0),
        VehicleSpec("CAR", "Renault", "Logan 1.0 / 1.6 SCe", "1.0 SCe / 1.6 SCe Flex", "5W30 ou 5W40 Sintético Elf", "3.8 Litros", "32 PSI Diant. / 30 PSI Tras.", "50 Litros", "Flex", "NGK LZKAR7A", "DOT 4", "Glaceol RX Type D", 10000.0),
        VehicleSpec("CAR", "Renault", "Oroch 1.6 / 1.3 Turbo", "1.6 SCe / 1.3 TCe Turbo Flex", "5W30 Sintético Elf", "4.5 Litros", "32 PSI Diant. / 35 PSI Tras.", "50 Litros", "Flex", "NGK Iridium", "DOT 4", "Glaceol RX Amarelo", 10000.0),

        // ==========================================
        // CARROS - FORD
        // ==========================================
        VehicleSpec("CAR", "Ford", "Ka 1.0 Ti-VCT 3 Cils", "1.0 12V Ti-VCT 3 Cils (85cv)", "5W20 100% Sintético Motorcraft WSS-M2C948-B", "4.1 Litros", "32 PSI Diant. / 32 PSI Tras.", "51 Litros", "Flex", "NGK SILZKG7B9E (Laser Iridium)", "DOT 4 LV", "Motorcraft Laranja/Rosa (5.5L)", 10000.0),
        VehicleSpec("CAR", "Ford", "Ka 1.5 3 Cils Dragon", "1.5 12V Dragon Flex (136cv)", "5W20 100% Sintético Motorcraft", "4.25 Litros", "32 PSI Diant. / 32 PSI Tras.", "51 Litros", "Flex", "NGK Iridium", "DOT 4 LV", "Motorcraft Rosa", 10000.0),
        VehicleSpec("CAR", "Ford", "EcoSport 1.5 Dragon / 2.0", "1.5 Dragon Flex / 2.0 Direct Flex", "5W20 ou 5W30 Sintético Motorcraft", "4.3 Litros", "32 PSI Diant. / 30 PSI Tras.", "52 Litros", "Flex", "NGK Iridium", "DOT 4 LV", "Motorcraft Rosa", 10000.0),
        VehicleSpec("CAR", "Ford", "Fiesta 1.6 Sigma", "1.6 16V Sigma Flex (128cv)", "5W30 100% Sintético Motorcraft 913-D", "4.05 Litros", "32 PSI Diant. / 30 PSI Tras.", "51 Litros", "Flex", "NGK TR6B-10", "DOT 4", "Motorcraft Rosa (6.0L)", 10000.0),
        VehicleSpec("CAR", "Ford", "Focus 2.0 Direct Flex", "2.0 Duratec Direct Flex (178cv)", "5W20 ou 5W30 Sintético Motorcraft", "4.3 Litros", "32 PSI Diant. / 32 PSI Tras.", "55 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Motorcraft Rosa", 10000.0),
        VehicleSpec("CAR", "Ford", "Ranger 2.2 / 3.2 / 3.0 V6 Diesel", "3.2 Duratorq (200cv) / 3.0 V6 Turbo Diesel", "5W30 100% Sintético ACEA C1/C2", "8.5 Litros", "35 PSI Diant. / 38 PSI Tras.", "80 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "Motorcraft Rosa (10L)", 10000.0),

        // ==========================================
        // CARROS - JEEP
        // ==========================================
        VehicleSpec("CAR", "Jeep", "Renegade 1.3 Turbo T270", "1.3 16V Turbo Flex GSE (185cv)", "0W20 100% Sintético Mopar Selènia", "4.8 Litros", "35 PSI Diant. / 35 PSI Tras.", "55 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Mopar Rosa Orgânico", 10000.0),
        VehicleSpec("CAR", "Jeep", "Renegade 1.8 16V E.torQ", "1.8 16V E.torQ EVO Flex (139cv)", "5W30 100% Sintético Mopar Selènia", "4.3 Litros", "35 PSI Diant. / 35 PSI Tras.", "60 Litros", "Flex", "NGK Iridium", "DOT 4", "Mopar Rosa (6.0L)", 10000.0),
        VehicleSpec("CAR", "Jeep", "Compass 1.3 Turbo T270 / 2.0 TD", "1.3 Turbo Flex (185cv) / 2.0 Turbo Diesel (170cv)", "0W20 ou 5W30 Sintético Low SAPS", "4.8 Litros", "35 PSI Diant. / 35 PSI Tras.", "60 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Mopar Orgânico", 10000.0),
        VehicleSpec("CAR", "Jeep", "Commander 1.3 Turbo / 2.0 Diesel", "1.3 Turbo 270 Flex / 2.0 TD 380 Diesel", "0W20 ou 5W30 Sintético", "4.8 Litros", "36 PSI Diant. / 36 PSI Tras.", "61 Litros", "Flex", "NGK Iridium", "DOT 4", "Mopar Orgânico", 10000.0),

        // ==========================================
        // CARROS - NISSAN
        // ==========================================
        VehicleSpec("CAR", "Nissan", "Kicks 1.6 16V", "1.6 16V HR16DE Flex (114cv)", "0W20 ou 5W30 100% Sintético Nissan Genuine", "3.2 Litros", "32 PSI Diant. / 30 PSI Tras.", "41 Litros", "Flex", "NGK PLZKAR6A-11 (Platinum)", "DOT 4", "Nissan L255N Long Life Azul", 10000.0),
        VehicleSpec("CAR", "Nissan", "Versa / March 1.6 16V", "1.6 16V HR16DE Flex (111cv)", "0W20 ou 5W30 Sintético Nissan", "3.2 Litros", "32 PSI Diant. / 30 PSI Tras.", "41 Litros", "Flex", "NGK Platinum", "DOT 4", "Nissan L255N Azul", 10000.0),
        VehicleSpec("CAR", "Nissan", "Frontier 2.3 Bi-Turbo Diesel", "2.3 16V Bi-Turbo Diesel (190cv)", "5W30 Sintético ACEA C4 (Low SAPS)", "6.7 Litros", "35 PSI Diant. / 38 PSI Tras.", "80 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "Nissan Coolant (8.5L)", 10000.0),

        // ==========================================
        // CARROS - PEUGEOT / CITROËN
        // ==========================================
        VehicleSpec("CAR", "Peugeot", "208 1.0 Firefly / 1.0 Turbo", "1.0 Firefly (75cv) / 1.0 Turbo 200 (130cv)", "0W20 Sintético Selènia Forward", "3.3 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "NGK Iridium", "DOT 4", "Petronas Paraflu UP", 10000.0),
        VehicleSpec("CAR", "Peugeot", "2008 1.6 THP / 1.0 Turbo", "1.6 Turbo THP (173cv) / 1.0 Turbo", "5W30 ou 0W20 Sintético ACEA C2/C3", "4.25 Litros", "32 PSI Diant. / 32 PSI Tras.", "55 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Orgânico Rosa", 10000.0),
        VehicleSpec("CAR", "Citroën", "C3 1.0 Firefly / 1.6", "1.0 6V Firefly (75cv) / 1.6 16V EC5", "0W20 ou 5W30 100% Sintético", "3.2 Litros", "32 PSI Diant. / 32 PSI Tras.", "47 Litros", "Flex", "NGK Iridium", "DOT 4", "Orgânico Rosa", 10000.0),
        VehicleSpec("CAR", "Citroën", "C4 Cactus 1.6 Flex / THP", "1.6 16V Flex (118cv) / 1.6 THP Turbo (173cv)", "5W30 100% Sintético Total Ineo", "4.25 Litros", "32 PSI Diant. / 32 PSI Tras.", "55 Litros", "Flex", "NGK Laser Iridium", "DOT 4", "Orgânico Total", 10000.0),

        // ==========================================
        // MOTOS - HONDA
        // ==========================================
        VehicleSpec("MOTO", "Honda", "CG 160 Fan / Titan / Cargo", "162.7cc OHC Monocilíndrico 4T (15.1cv)", "10W30 Semissintético API SL JASO MA (Honda Genuine)", "1.2 Litros (1.0L na troca)", "25 PSI Diant. / 29 PSI Tras. (33 c/ garupa)", "16.1 Litros", "Flex", "NGK CPR8EA-9", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "CG 160 Start", "162.7cc OHC Monocilíndrico (14.9cv)", "10W30 Semissintético JASO MA", "1.2 Litros (1.0L na troca)", "25 PSI Diant. / 29 PSI Tras.", "14.6 Litros", "Gasolina", "NGK CPR8EA-9", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "Biz 125 Flex", "124.9cc OHC Monocilíndrico 4T (9.2cv)", "10W30 Semissintético JASO MA", "0.9 Litros (0.7L na troca)", "25 PSI Diant. / 29 PSI Tras. (33 c/ garupa)", "5.1 Litros", "Flex", "NGK CPR6EA-9S / CPR7EA-9", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "Biz 110i", "109.1cc OHC 4T Injeção (8.3cv)", "10W30 Semissintético JASO MA", "0.9 Litros (0.7L na troca)", "25 PSI Diant. / 29 PSI Tras.", "5.1 Litros", "Gasolina", "NGK CPR6EA-9S", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "NXR 160 Bros", "162.7cc OHC 4T Flex (14.7cv)", "10W30 Semissintético JASO MA", "1.2 Litros (1.0L na troca)", "22 PSI Diant. / 22 PSI Tras. (29 c/ garupa)", "12.0 Litros", "Flex", "NGK CPR8EA-9", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "Pop 110i", "109.1cc OHC 4T Monocilíndrico (7.9cv)", "10W30 Semissintético JASO MA", "1.0 Litros (0.8L na troca)", "25 PSI Diant. / 29 PSI Tras.", "4.2 Litros", "Gasolina", "NGK CPR6EA-9S", "Tambor/Mecânico", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "CB 300F Twister", "293.5cc OHC 4V Monocilíndrico (24.7cv)", "10W30 100% Sintético ou Semissintético JASO MA", "1.8 Litros (1.5L na troca c/ filtro)", "29 PSI Diant. / 33 PSI Tras.", "14.1 Litros", "Flex", "NGK LMAR8A-9", "DOT 4", "Radiador de Óleo / Ar", 6000.0),
        VehicleSpec("MOTO", "Honda", "CB 250F Twister", "249.5cc OHC 4V Flex (22.6cv)", "10W30 Semissintético JASO MA", "1.8 Litros (1.4L na troca)", "29 PSI Diant. / 33 PSI Tras.", "16.5 Litros", "Flex", "NGK SIMR8A9 (Iridium)", "DOT 4", "Radiador de Óleo / Ar", 6000.0),
        VehicleSpec("MOTO", "Honda", "XRE 300 / Sahara 300", "293.5cc / 291.6cc DOHC Flex", "10W30 Semissintético JASO MA", "1.8 Litros (1.5L na troca)", "22 PSI Diant. / 22 PSI Tras. (29 c/ garupa)", "13.8 Litros", "Flex", "NGK SIMR8A9", "DOT 4", "Radiador de Óleo / Ar", 6000.0),
        VehicleSpec("MOTO", "Honda", "XRE 190", "184.4cc OHC 4T Flex (16.4cv)", "10W30 Semissintético JASO MA", "1.2 Litros (1.0L na troca)", "22 PSI Diant. / 22 PSI Tras.", "13.5 Litros", "Flex", "NGK CPR8EA-9", "DOT 4", "Arrefecimento a Ar", 4000.0),
        VehicleSpec("MOTO", "Honda", "PCX 160", "156.9cc eSP+ 4V Monocilíndrico (16cv)", "10W30 Semissintético JASO MB (Específico Scooter)", "0.9 Litros (0.8L na troca)", "29 PSI Diant. / 33 PSI Tras. (36 c/ garupa)", "8.0 Litros", "Gasolina", "NGK LMAR8L-9", "DOT 4", "Líquido Arrefecimento 50/50 (0.6L)", 6000.0),
        VehicleSpec("MOTO", "Honda", "ADV 150", "149.3cc eSP Monocilíndrico 4T (13.2cv)", "10W30 Semissintético JASO MB Scooter", "0.9 Litros (0.8L na troca)", "29 PSI Diant. / 33 PSI Tras.", "8.0 Litros", "Gasolina", "NGK CPR8EA-9", "DOT 4", "Líquido Honda Coolant", 6000.0),
        VehicleSpec("MOTO", "Honda", "CB 500X / CB 500F", "471cc DOHC 8V Bicilíndrico (50.4cv)", "10W30 100% Sintético JASO MA2", "3.2 Litros (2.7L na troca c/ filtro)", "36 PSI Diant. / 42 PSI Tras.", "17.7 Litros", "Gasolina", "NGK CPR8EA-9", "DOT 4", "Líquido Honda Coolant (1.4L)", 6000.0),
        VehicleSpec("MOTO", "Honda", "CB 650R / CBR 650R", "649cc DOHC 16V 4 Cilindros (88.4cv)", "10W30 100% Sintético JASO MA2", "3.0 Litros (2.6L na troca)", "36 PSI Diant. / 42 PSI Tras.", "15.4 Litros", "Gasolina", "NGK IMR9E-9HES (Iridium)", "DOT 4", "Honda Coolant (2.3L)", 6000.0),
        VehicleSpec("MOTO", "Honda", "NC 750X", "745cc SOHC 8V Bicilíndrico (58.6cv)", "10W30 100% Sintético JASO MA2", "3.7 Litros (3.2L na troca)", "36 PSI Diant. / 42 PSI Tras.", "14.1 Litros", "Gasolina", "NGK IFR6G-11K (Iridium)", "DOT 4", "Líquido Orgânico (1.7L)", 10000.0),

        // ==========================================
        // MOTOS - YAMAHA
        // ==========================================
        VehicleSpec("MOTO", "Yamaha", "Factor 150 UBS", "149cc SOHC 2V BlueFlex (12.4cv)", "20W50 ou 10W40 Semissintético Yamalube 4T", "1.2 Litros (1.0L na troca)", "25 PSI Diant. / 29 PSI Tras. (33 c/ garupa)", "15.7 Litros", "Flex", "NGK CR8E", "DOT 4", "Arrefecimento a Ar", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Factor 125i UBS", "125cc SOHC 2V Injeção (11cv)", "20W50 ou 10W40 Yamalube 4T", "1.2 Litros (1.0L na troca)", "25 PSI Diant. / 29 PSI Tras.", "15.7 Litros", "Flex", "NGK CR8E", "DOT 4", "Arrefecimento a Ar", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Fazer FZ15 ABS", "149cc SOHC 2V BlueFlex (12.4cv)", "10W40 Semissintético Yamalube 4T", "1.2 Litros (1.0L na troca)", "25 PSI Diant. / 29 PSI Tras.", "11.9 Litros", "Flex", "NGK CR8E", "DOT 4", "Arrefecimento a Ar", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Fazer FZ25 ABS", "249cc SOHC 2V BlueFlex (21.5cv)", "10W40 ou 20W50 Yamalube JASO MA2", "1.55 Litros (1.35L na troca c/ filtro)", "29 PSI Diant. / 33 PSI Tras.", "14.0 Litros", "Flex", "NGK DR8EA", "DOT 4", "Radiador de Óleo", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Lander 250 ABS", "249cc SOHC 2V BlueFlex (20.9cv)", "10W40 Yamalube JASO MA2", "1.55 Litros (1.35L na troca c/ filtro)", "22 PSI Diant. / 22 PSI Tras. (25 c/ garupa)", "13.6 Litros", "Flex", "NGK DR8EA", "DOT 4", "Radiador de Óleo", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Crosser 150 S / Z", "149cc SOHC 2V BlueFlex (12.4cv)", "20W50 ou 10W40 Yamalube 4T", "1.2 Litros (1.0L na troca)", "22 PSI Diant. / 22 PSI Tras. (29 c/ garupa)", "12.0 Litros", "Flex", "NGK CR8E", "DOT 4", "Arrefecimento a Ar", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "NMax 160 Connected", "155cc SOHC 4V VVA Líquido (15.4cv)", "10W40 Yamalube 4T Scooter (JASO MB)", "1.0 Litros (0.9L na troca)", "29 PSI Diant. / 33 PSI Tras. (36 c/ garupa)", "7.1 Litros", "Gasolina", "NGK CPR8EA-9", "DOT 4", "Yamalube Coolant (0.5L)", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "Fluo 125 ABS", "125cc SOHC 2V Automático (9.5cv)", "10W40 Yamalube Scooter JASO MB", "0.9 Litros (0.8L na troca)", "29 PSI Diant. / 33 PSI Tras.", "4.2 Litros", "Gasolina", "NGK CR6HSA", "DOT 4", "Arrefecimento a Ar", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "XMAX 250 ABS", "250cc SOHC 4V Líquido (22.8cv)", "10W40 Yamalube Scooter JASO MB", "1.7 Litros (1.5L na troca)", "29 PSI Diant. / 33 PSI Tras.", "13.0 Litros", "Gasolina", "NGK LMAR8A-9", "DOT 4", "Yamalube Coolant (1.1L)", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "MT-03 ABS / YZF-R3", "321cc DOHC 8V Bicilíndrico (42cv)", "10W40 100% Sintético Yamalube RS4GP", "2.4 Litros (2.1L na troca c/ filtro)", "29 PSI Diant. / 36 PSI Tras.", "14.0 Litros", "Gasolina", "NGK LMAR8A-9", "DOT 4", "Yamaha Coolant 50/50 (1.8L)", 5000.0),
        VehicleSpec("MOTO", "Yamaha", "MT-07 ABS", "689cc DOHC 8V Crossplane CP2 (74.8cv)", "10W40 100% Sintético Yamalube", "3.0 Litros (2.6L na troca c/ filtro)", "36 PSI Diant. / 42 PSI Tras.", "14.0 Litros", "Gasolina", "NGK LMAR8A-9", "DOT 4", "Yamaha Coolant (1.9L)", 10000.0),
        VehicleSpec("MOTO", "Yamaha", "MT-09 ABS", "890cc DOHC 12V Crossplane CP3 (119cv)", "10W40 100% Sintético Yamalube", "3.5 Litros (3.2L na troca c/ filtro)", "36 PSI Diant. / 42 PSI Tras.", "14.0 Litros", "Gasolina", "NGK CPR9EA-9", "DOT 4", "Yamaha Coolant (2.3L)", 10000.0),

        // ==========================================
        // MOTOS - BMW
        // ==========================================
        VehicleSpec("MOTO", "BMW", "G 310 GS / G 310 R", "313cc DOHC 4V Monocilíndrico (34cv)", "15W50 ou 5W40 100% Sintético BMW Advantec", "1.7 Litros (1.6L na troca c/ filtro)", "25 PSI Diant. / 28 PSI Tras. (32 c/ garupa)", "11.0 Litros", "Gasolina", "NGK LMAR9D-J", "DOT 4", "BMW Coolant Orgânico (1.0L)", 10000.0),
        VehicleSpec("MOTO", "BMW", "F 850 GS / F 750 GS", "853cc DOHC Bicilíndrico Paralelo (85cv)", "5W40 100% Sintético BMW Advantec", "3.0 Litros (2.8L na troca c/ filtro)", "32 PSI Diant. / 36 PSI Tras.", "15.0 Litros", "Gasolina", "NGK Iridium", "DOT 4", "BMW Coolant Azul", 10000.0),
        VehicleSpec("MOTO", "BMW", "R 1250 GS / Adventure", "1254cc Boxer ShiftCam Bicilíndrico (136cv)", "5W40 100% Sintético BMW Advantec Pro", "4.0 Litros", "36 PSI Diant. / 42 PSI Tras.", "20.0 Litros", "Gasolina", "NGK LMAR8J-9E", "DOT 4", "BMW Coolant Híbrido", 10000.0),

        // ==========================================
        // MOTOS - KAWASAKI
        // ==========================================
        VehicleSpec("MOTO", "Kawasaki", "Ninja 400 / Z400", "399cc DOHC 8V Bicilíndrico (48cv)", "10W40 100% Sintético JASO MA2", "2.3 Litros (2.0L na troca c/ filtro)", "28 PSI Diant. / 32 PSI Tras.", "14.0 Litros", "Gasolina", "NGK LMAR9G", "DOT 4", "Kawasaki Aluma-Cool 50/50 (1.3L)", 6000.0),
        VehicleSpec("MOTO", "Kawasaki", "Versys 650 / Ninja 650", "649cc DOHC 8V Bicilíndrico (67cv)", "10W40 100% Sintético JASO MA2", "2.6 Litros (2.3L na troca)", "32 PSI Diant. / 36 PSI Tras.", "21.0 Litros", "Gasolina", "NGK SILMAR9B9", "DOT 4", "Kawasaki Coolant (1.7L)", 6000.0),
        VehicleSpec("MOTO", "Kawasaki", "Z900 ABS", "948cc DOHC 16V 4 Cilindros (125cv)", "10W40 100% Sintético JASO MA2", "4.0 Litros (3.8L na troca c/ filtro)", "36 PSI Diant. / 42 PSI Tras.", "17.0 Litros", "Gasolina", "NGK CR9EIA-9 (Iridium)", "DOT 4", "Kawasaki Coolant (2.5L)", 6000.0),

        // ==========================================
        // MOTOS - ROYAL ENFIELD
        // ==========================================
        VehicleSpec("MOTO", "Royal Enfield", "Hunter 350 / Meteor 350", "349cc SOHC Monocilíndrico J-Series (20.2cv)", "15W50 Semissintético ou Sintético JASO MA2", "2.0 Litros (1.7L na troca c/ filtro)", "29 PSI Diant. / 32 PSI Tras. (36 c/ garupa)", "13.0 Litros", "Gasolina", "Bosch YR7MES / NGK CPR8EA-9", "DOT 4", "Radiador de Óleo / Ar", 5000.0),
        VehicleSpec("MOTO", "Royal Enfield", "Himalayan 411", "411cc SOHC Monocilíndrico LS410 (24.5cv)", "15W50 Semissintético JASO MA2", "2.2 Litros (2.0L na troca)", "22 PSI Diant. / 32 PSI Tras.", "15.0 Litros", "Gasolina", "NGK CR8E", "DOT 4", "Radiador de Óleo", 5000.0),
        VehicleSpec("MOTO", "Royal Enfield", "Interceptor 650 / Continental GT", "648cc SOHC 8V Bicilíndrico (47cv)", "10W50 100% Sintético JASO MA2", "3.1 Litros (2.8L na troca c/ filtro)", "32 PSI Diant. / 36 PSI Tras.", "13.7 Litros", "Gasolina", "Bosch UR5CC", "DOT 4", "Radiador de Óleo", 5000.0),

        // ==========================================
        // MOTOS - SUZUKI / SHINERAY / BAJAJ
        // ==========================================
        VehicleSpec("MOTO", "Suzuki", "Yes 125 EN / Intruder 125", "124cc OHC 4T (12cv)", "20W50 ou 10W40 Motul / Yamalube", "1.1 Litros (0.9L na troca)", "25 PSI Diant. / 29 PSI Tras.", "14.0 Litros", "Gasolina", "NGK D8EA / DR8EA", "DOT 4", "Arrefecimento a Ar", 3000.0),
        VehicleSpec("MOTO", "Suzuki", "V-Strom 650 XT", "645cc DOHC 8V V-Twin (71cv)", "10W40 Sintético Ecstar Suzuki", "3.0 Litros (2.6L na troca c/ filtro)", "33 PSI Diant. / 36 PSI Tras. (41 c/ garupa)", "20.0 Litros", "Gasolina", "NGK MR8E-9", "DOT 4", "Suzuki Coolant (1.9L)", 6000.0),
        VehicleSpec("MOTO", "Shineray", "Jet 125 / Phoenix 50", "123.7cc / 49cc Monocilíndrico 4T", "20W50 Mineral ou Semissintético JASO MA", "0.9 Litros (0.8L na troca)", "25 PSI Diant. / 28 PSI Tras.", "4.0 Litros", "Gasolina", "NGK C7HSA", "DOT 4", "Arrefecimento a Ar", 3000.0),
        VehicleSpec("MOTO", "Bajaj", "Dominar 400 / 200", "373cc / 199cc DOHC 4V Líquido (40cv)", "10W50 100% Sintético JASO MA2", "1.7 Litros (1.5L na troca)", "29 PSI Diant. / 32 PSI Tras.", "13.0 Litros", "Gasolina", "Bosch VR5NE / NGK", "DOT 4", "Líquido Orgânico 50/50", 5000.0),

        // ==========================================
        // CAMINHÕES - MERCEDES-BENZ / SCANIA / VOLVO / VW
        // ==========================================
        VehicleSpec("TRUCK", "Mercedes-Benz", "Accelo 1016 / 1316", "4.8 4 Cils Turbo Diesel OM 924 (156cv)", "15W40 Mineral ou 10W40 Semissintético MB 228.3", "15.0 Litros", "110 PSI Diant. / 110 PSI Tras.", "150 Litros", "Diesel", "Velas Aquecedoras Diesel", "DOT 4 / Ar Pneumático", "Orgânico Rosa (20L)", 20000.0),
        VehicleSpec("TRUCK", "Mercedes-Benz", "Atego 2426 / 1719", "7.2 6 Cils Turbo Diesel OM 926 (256cv)", "15W40 MB 228.3 / 10W40 MB 228.51", "28.0 Litros", "115 PSI Diant. / 115 PSI Tras.", "300 Litros", "Diesel", "Velas Aquecedoras", "Freio Pneumático", "Orgânico Rosa (30L)", 25000.0),
        VehicleSpec("TRUCK", "Volkswagen", "Delivery 9.170 / 11.180", "3.8 Cummins ISF Turbo Intercooler (175cv)", "15W40 API CJ-4 / CK-4 Cummins", "12.0 Litros", "100 PSI Diant. / 105 PSI Tras.", "150 Litros", "Diesel", "Velas Aquecedoras", "Pneumático c/ ABS", "VW G12 Rosa (16L)", 20000.0),
        VehicleSpec("TRUCK", "Volkswagen", "Constellation 24.280", "6.9 6 Cils MAN D0836 (275cv)", "15W40 ou 10W40 MAN M 3277", "27.5 Litros", "110 PSI Diant. / 115 PSI Tras.", "275 Litros", "Diesel", "Velas Aquecedoras", "Pneumático", "VW G12 Rosa (28L)", 25000.0),
        VehicleSpec("TRUCK", "Volvo", "FH 540 / FH 460", "12.8 6 Cils D13K Turbo Diesel (540cv)", "10W40 ou 15W40 VDS-4.5 Volvo", "36.0 Litros", "120 PSI Diant. / 120 PSI Tras.", "800 Litros", "Diesel", "Velas Aquecedoras", "Pneumático EBS", "Volvo VCS Amarelo (40L)", 30000.0),
        VehicleSpec("TRUCK", "Scania", "R450 / R500", "13.0 6 Cils DC13 Turbo Diesel (450cv/500cv)", "10W40 ou 15W40 Scania LDF-3/LDF-4", "38.0 Litros", "120 PSI Diant. / 120 PSI Tras.", "750 Litros", "Diesel", "Velas Aquecedoras", "Pneumático EBS", "Scania Coolant (42L)", 30000.0),
        VehicleSpec("TRUCK", "Iveco", "Daily 35S14 / 30-130", "3.0 16V Turbo Diesel F1C (146cv)", "5W30 Sintético Urania Daily ACEA C2", "7.0 Litros", "65 PSI Diant. / 65 PSI Tras.", "90 Litros", "Diesel", "Velas Aquecedoras", "DOT 4", "Paraflu 11 (10L)", 15000.0)
    )

    fun getBrandsForType(type: String): List<String> {
        val brands = specsCatalog
            .filter { it.type == type }
            .map { it.brand }
            .distinct()
            .sorted()
        return if (brands.isNotEmpty()) brands else listOf("Outra Marca")
    }

    fun getModelsForBrand(type: String, brand: String): List<String> {
        return specsCatalog
            .filter { it.type == type && it.brand.equals(brand, ignoreCase = true) }
            .map { it.model }
            .distinct()
            .sorted()
    }

    fun findSpec(type: String, brand: String, model: String): VehicleSpec? {
        return specsCatalog.find { 
            it.type == type && 
            it.brand.equals(brand, ignoreCase = true) && 
            it.model.equals(model, ignoreCase = true) 
        } ?: specsCatalog.find {
            it.type == type &&
            (model.contains(it.model, ignoreCase = true) || it.model.contains(model, ignoreCase = true))
        } ?: specsCatalog.find {
            it.type == type && it.brand.equals(brand, ignoreCase = true)
        }
    }

    fun searchSpecs(query: String, typeFilter: String? = null): List<VehicleSpec> {
        val q = query.trim().lowercase()
        return specsCatalog.filter { spec ->
            (typeFilter == null || spec.type == typeFilter) &&
            (q.isEmpty() || 
             spec.brand.lowercase().contains(q) || 
             spec.model.lowercase().contains(q) || 
             spec.engine.lowercase().contains(q))
        }
    }
}
