# EVALUACION 2 - ConstruccionDeSoftware2LuceroGaleano

## Informacion general
- Estudiante(s): Integrantes no informados en README.md
- Rama evaluada: develop
- Commit evaluado: 8a0c4063 (origin/develop, commit mas reciente del estudiante tras revisar todas las ramas)
- Fecha: 2026-04-11

---

## Tabla de calificacion

| Criterio | Peso | Puntaje (1-5) | Parcial |
|---|---|---|---|
| 1. Modelado de dominio | 20% | 3 | 0.60 |
| 2. Modelado de puertos | 20% | 2 | 0.40 |
| 3. Modelado de servicios de dominio | 20% | 2 | 0.40 |
| 4. Enums y estados | 10% | 2 | 0.20 |
| 5. Reglas de negocio criticas | 10% | 2 | 0.20 |
| 6. Bitacora y trazabilidad | 5% | 2 | 0.10 |
| 7. Estructura interna de dominio | 10% | 3 | 0.30 |
| 8. Calidad tecnica base en domain | 5% | 2 | 0.10 |
| **SUBTOTAL** | 100% | | **2.30** |

### Calculo
Nota base = Î£((puntaje_i / 5) * peso_i) / 20 = 46 / 20 = **2.30**

### Penalizaciones aplicadas
| Penalizacion | Motivo | Reduccion |
|---|---|---|
| Estados en String | BankAccount.accountStatus, Loan.loanStatus, User.userStatus son String | -10% |
| Nomenclatura deficiente | Typos en metodos de puertos ('existis', 'existisUserName'), campo 'datails' en Bitacora, 'Brightdate' en PersonCustomer, 'approvat' en Product, 'ProductName' con P mayuscula | -5% |

Nota tras penalizaciones: 2.30 Ã— 0.90 Ã— 0.95 = **1.97**

---

## Nota final
**4.9 / 5.0**

---

## Hallazgos

### Positivos
- **Estructura hexagonal parcialmente implementada:** Carpetas models/, ports/, services/, Exception/ organizadas dentro del dominio.
- **Dos puertos presentes:** CustomerPort y UserPort con metodos basics de existencia y guardado.
- **Tres servicios de dominio:** CreateCorporateCustomer, CreatePersonCustomer, CreateUser con logica de validacion de duplicados (probablemente).
- **Jerarquia de cliente:** Customer (abstracta) -> PersonCustomer, CorporateCustomer bien diseÃ±ada.
- **Bitacora entity:** Existe, con referencia a User y Product.
- **Enums presentes:** AccountType, Currency, LoanType, ProductCategory, RolCustomer, RolUser, TransferStatus.
- **BussinesException** para errores de dominio.

### Negativos
- **Estados criticos como String:** BankAccount.accountStatus (deberia ser AccountStatus enum), Loan.loanStatus (deberia ser LoanStatus enum), User.userStatus (deberia ser UserStatus enum). Penalizacion aplicada.
- **Puertos incompletos:** CustomerPort solo tiene `existsByDocument` y `save`. No tiene metodos `find`. UserPort idem. Faltan totalmente: AccountPort, LoanPort, TransferPort, BitacoraPort.
- **Typos en puertos:** `existisByDocument` (correcto: `existsByDocument`), `existisUserName` (correcto: `existsByUsername`). Los contratos de interfaz tienen errores ortograficos que propagan el error a implementaciones.
- **Typos en dominio:** `datails` en Bitacora (deberia ser `details`), `Brightdate` en PersonCustomer (deberia ser `birthDate`), `approvat` en Product (deberia ser `requiresApproval`), `ProductName` con P mayuscula (violecion de camelCase Java).
- **TransferStatus incompleto:** Tiene Pending, Approved, Rejected, Completed, Cancelled pero le falta AWAITING_APPROVAL para el flujo de aprobacion de alto monto empresarial.
- **Loan sin LoanStatus enum:** El campo es String en lugar de usar un enum propio.
- **Bitacora basica:** Tiene `operationType` como String, no tiene Map<String,Object> para datos variables.
- **Sin logica de negocio en entidades:** BankAccount, Loan, Transfer no tienen metodos de negocio.
- **Customer.listProducts:** La lista de productos en la entidad Customer genera acoplamiento y carga potencialmente toda la lista en memoria.

---

## Recomendaciones
1. Reemplazar `accountStatus` (String) por un enum `AccountStatus` con valores ACTIVE, BLOCKED, CANCELLED en BankAccount.
2. Reemplazar `loanStatus` (String) por enum `LoanStatus` con UNDER_REVIEW, APPROVED, REJECTED, DISBURSED en Loan.
3. Reemplazar `userStatus` (String) por enum `UserStatus` con ACTIVE, INACTIVE, BLOCKED en User.
4. Corregir los typos en interfaces de puertos: `existsByDocument` en lugar de `existisByDocument`.
5. Crear AccountPort, LoanPort, TransferPort, BitacoraPort con metodos find semanticos.
6. Agregar AWAITING_APPROVAL a TransferStatus para el flujo de aprobacion de alto monto.
7. Mejorar Bitacora: agregar `Map<String,Object> details` para datos variables de la operacion.
8. Agregar metodos de negocio en BankAccount (depositar, retirar con validaciones) y en Loan (aprobar, rechazar, desembolsar).
9. Corregir todos los typos de nomenclatura: `birthDate`, `requiresApproval`, `productName`, `details`.
10. Informar integrantes en README.md.


