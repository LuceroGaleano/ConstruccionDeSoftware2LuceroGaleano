# EVALUACIÓN - ConstruccionDeSoftware2LuceroGaleano

## Información General
- **Estudiante:** Lucero Galeano
- **Rama evaluada:** develop
- **Fecha de evaluación:** 2026-03-23

---

## Tabla de Calificación

| # | Criterio | Peso | Puntaje (1–5) | Nota ponderada |
|---|---|---|---|---|
| 1 | Modelado de dominio | 25% | 4 | 1.00 |
| 2 | Relaciones entre entidades | 15% | 4 | 0.60 |
| 3 | Uso de Enums | 15% | 3 | 0.45 |
| 4 | Manejo de estados | 5% | 1 | 0.05 |
| 5 | Tipos de datos | 5% | 2 | 0.10 |
| 6 | Separación Usuario vs Cliente | 10% | 5 | 0.50 |
| 7 | Bitácora | 5% | 2 | 0.10 |
| 8 | Reglas básicas de negocio | 5% | 3 | 0.15 |
| 9 | Estructura del proyecto | 10% | 4 | 0.40 |
| 10 | Repositorio | 10% | 3 | 0.30 |
| **TOTAL** | | **100%** | | **3.65 / 5 (base)** |

> Nota base = (4/5×0.25 + 4/5×0.15 + 3/5×0.15 + 1/5×0.05 + 2/5×0.05 + 5/5×0.10 + 2/5×0.05 + 3/5×0.05 + 4/5×0.10 + 3/5×0.10) × 5 = 0.73 × 5 = **3.65**

---

## Penalizaciones

| Penalización | Descuento | Nota resultante |
|---|---|---|
| Variables mal nombradas (`Brightdate`, `datails`, `approvat`, `OriginAccount`, `ProductName` — typos y capitalization incorrecta) | -10% | 3.65 × 0.90 = **3.29** |

---

## Bonus

| Bonus | % |
|---|---|
| Herencia correcta: `Customer` (abstracta) → `PersonCustomer` / `CorporateCustomer` | +2% |

> Nota con bonus = 3.29 × 1.02 = **3.36 → 3.4**

---

## Nota Final: **3.4 / 5.0**

---

## Análisis por Criterio

### 1. Modelado de dominio — 4/5
Entidades implementadas: `Person` (abstracta), `Customer` (abstracta, extiende `Person`), `PersonCustomer`, `CorporateCustomer`, `User`, `Product` (abstracta), `BankAccount` (extiende `Product`), `Loan` (extiende `Product`), `Transfer`, `Bitacora`.  
`Product` como clase abstracta base para `BankAccount` y `Loan` es una decisión de diseño interesante y válida desde DDD.  
**Observación:** No existe una clase `ProductoBancario` separada (catálogo). `Product` es la base de los productos financieros concretos, no un catálogo. La entidad catálogo (`GeneralBankProduct`) está ausente como clase independiente.

### 2. Relaciones entre entidades — 4/5
`Product` tiene `customerOwner: Customer` ✓ — `BankAccount` y `Loan` heredan esta relación.  
`Loan` tiene `customerApplicant: Customer` ✓ y `disburseAccount: BankAccount` ✓.  
`Transfer` tiene `OriginAccount: BankAccount` y `DestinationAccount: BankAccount` ✓.  
`CorporateCustomer` tiene `legalRepresentative: PersonCustomer` ✓.  
`Bitacora` tiene `user: User` ✓ y `product: Product` ✓.  
Relaciones mayoritariamente correctas con referencias a objetos de dominio.

### 3. Uso de Enums — 3/5
Enums presentes: `AccountType`, `Currency`, `LoanType`, `TransferStatus`, `RolUser`, `RolCustomer`, `ProductCategory` ✓  
**Faltantes críticos:** `AccountStatus` (`accountStatus` en `BankAccount` es `String`), `EstadoPrestamo` (ausente — `loanStatus` en `Loan` es `String`), `UserStatus` (`userStatus` en `User` es `String`). La mezcla enums + String en los tres campos de estado principales es un problema importante.

### 4. Manejo de estados — 1/5
Los estados de las tres entidades clave se implementaron como `String`:
- `BankAccount.accountStatus: String`
- `Loan.loanStatus: String`
- `User.userStatus: String`

Solo `Transfer.transferStatus: TransferStatus` usa el enum correcto. Los estados más importantes del negocio no están tipados correctamente.

### 5. Tipos de datos — 2/5
`double` para montos monetarios (debería ser `BigDecimal`). `java.sql.Date` para fechas (debería ser `java.time.LocalDate`). El atributo `Bitacora.operationDate` y `operationTime` tienen fecha y hora separados en lugar de un único `LocalDateTime`.

### 6. Separación Usuario vs Cliente — 5/5
`User extends Person` y `Customer extends Person` son jerarquías completamente independientes. `User` tiene `userName`, `systemRole: RolUser` y `userStatus: String`; `Customer` tiene `rolCustomer: RolCustomer` y lista de productos. Separación perfecta.

### 7. Bitácora — 2/5
`Bitacora` existe con `operationType`, `operationDate`, `operationTime`, `user`, `product` y `datails: String`. El campo `datails` (con typo) es un `String` simple — no es la estructura flexible `Map<String, Object>` requerida. El nombre del campo tiene además un error tipográfico (`datails` en lugar de `details`).

### 8. Reglas básicas de negocio — 3/5
Capa de servicios implementada con reglas de negocio reales:
- `CreatePersonCustomer.createPersonCustomer()` valida documento duplicado ✓
- `CreateCorporateCustomer.createCorporateCustomer()` valida NIT y representante legal ✓
- `CreateUser.createUser()` valida documento y nombre de usuario únicos ✓
- Uso de `BussinesException` para errores de dominio ✓  
Puertos definidos: `CustomerPort`, `UserPort`. Faltan servicios para `BankAccount`, `Loan` y `Transfer`.

### 9. Estructura del proyecto — 4/5
Organización en paquetes: `domain/models`, `domain/Exception`, `domain/ports`, `domain/services`. Separación coherente con clean architecture básica. Falta completar los servicios para todas las entidades.

### 10. Repositorio — 3/5
- **Nombre:** `ConstruccionDeSoftware2LuceroGaleano` ✓ formato correcto.
- **Commits:** 6 commits con actividad progresiva en `develop`. Sin convención ADD/CHG; mensajes como "probando1", "create classes 1.0" son informales.
- **README:** Solo contiene el título del repositorio.
- **Ramas:** Tiene `develop` ✓.
- **Tag de entrega:** No existe.

---

## Fortalezas
- Jerarquía de clientes correcta: `Customer` → `PersonCustomer` / `CorporateCustomer`.
- Relaciones entre entidades bien implementadas con referencias a objetos.
- Capa de servicios con validaciones de negocio reales y manejo de excepciones de dominio.
- Buena estructura de paquetes con ports y servicios.
- `Transfer` usa correctamente `TransferStatus` enum.

## Oportunidades de mejora
- **Crítico:** Agregar los enums de estado faltantes: `AccountStatus`, `LoanStatus`, `UserStatus` y utilizarlos en las entidades correspondientes.
- Cambiar `Bitacora.datails: String` a `Map<String, Object>` para bitácora flexible.
- Agregar servicios de dominio para `BankAccount`, `Loan` y `Transfer`.
- Reemplazar `java.sql.Date` por `java.time.LocalDate` y fusionar `operationDate`/`operationTime` en un único `LocalDateTime`.
- Usar `BigDecimal` para montos monetarios.
- Corregir typos en nombres de variables: `Brightdate` → `birthDate`, `datails` → `details`, `approvat` → `requiresApproval`, etc.
- Completar el README con información del proyecto, tecnología e instrucciones.
- Agregar tag de entrega y mejorar los mensajes de commit.
