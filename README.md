# 🔐 Local DSC PDF Signer

Local DSC PDF Signer is a **Java Spring Boot application** that allows you to digitally sign PDF files using a **DSC token** connected to a Windows system.  
It exposes secure REST APIs to verify DSC presence, validate PIN, and digitally sign PDF documents.

This project is designed to work as a **local signing gateway** for enterprise applications such as DSpace, judicial systems, audit platforms, and document management systems.

---

## 🚀 Features

- Detect DSC token status
- Secure PIN verification
- PDF digital signing using DSC
- REST API based local gateway
- Works on Windows systems
- Supports remote API access over LAN

---

## 🏗 Architecture

The signing process is executed locally to ensure that the private key never leaves the DSC device, maintaining maximum security.
```
Client Application
        |
        | REST API
        |
Local Signer (Spring Boot)
        |
        | USB
        |
    DSC Token
```

---

## 🔗 API Endpoints

### 1️⃣ Check DSC Token Status
```http
GET http://host:port/signer/token/status
```

**Response**
```json
{
  "tokenPresent": "true"
}
```

### 2️⃣ Verify DSC Token PIN
```http
POST http://host:port/signer/token/verify-pin?pin=123456
```

### 3️⃣ Stamp the Signature in PDF
```http
POST http://host:port/signer/sign
```

**Request Body**
```json
{
  "pin": "123456",
  "filename": "document.pdf",
  "pdfBytes": "Base64EncodedPDF"
}
```

**Response**  
The signed PDF file is returned as a binary stream or Base64 encoded content.

---

## 💻 System Requirements

| Requirement | Value |
|------------|-------|
| OS | Windows |
| Java | JDK 8 or higher |
| Maven | 3.x |
| DSC Token Driver | Installed |
| USB Token | Valid DSC Token |

---

## 📦 Installation

### Step 1: Clone Repository
```bash
git clone https://github.com/suryanshu-15/local_signer.git
```

### Step 2: Build Project
```bash
cd local_signer
mvn clean package
```

The executable JAR will be generated at:
```
target/local-signer-0.0.1-SNAPSHOT.jar
```

### Step 3: Run Application
```bash
java -jar target/local-signer-0.0.1-SNAPSHOT.jar
```

The server will start on:
```
http://localhost:9001
```

---

---

## 🔄 Auto-Start Configuration (Windows Service)

To run the Local DSC PDF Signer automatically on Windows startup without manual intervention, you can install it as a Windows Service using NSSM (Non-Sucking Service Manager).

### Prerequisites

Download NSSM from: [https://nssm.cc/download](https://nssm.cc/download)

Extract it to a location (e.g., `D:\Downloads\nssm-2.24\`)

### Installation Steps

#### Step 1: Install Service using NSSM

Open Command Prompt or PowerShell as Administrator and run:
```bash
D:\Downloads\nssm-2.24\nssm-2.24\win64\nssm.exe install LocalSignerService
```

#### Step 2: Configure Service in NSSM GUI

**Application Tab:**
- **Path:** `C:\Program Files\Microsoft\jdk-17.0.11.9-hotspot\bin\java.exe` (adjust to your Java installation path)
- **Startup directory:** `D:\Downloads\local-signer\local-signer\target` (your project path)
- **Arguments:** `-jar D:\Downloads\local-signer\local-signer\target\local-signer-0.0.1-SNAPSHOT.jar`

**Details Tab:**
- **Display name:** Local DSC PDF Signer
- **Description:** Digital Signature Certificate PDF Signing Service
- **Startup type:** Automatic

**Log On Tab:**
- Select: **This account** → Enter your Windows login credentials
- (Required for DSC token access)

**I/O Tab:**
- **Output (stdout):** `D:\Downloads\local-signer\local-signer\target\service.log`
- **Error (stderr):** `D:\Downloads\local-signer\local-signer\target\service-error.log`

Click **Install service**

#### Step 3: Start the Service
```bash
D:\Downloads\nssm-2.24\nssm-2.24\win64\nssm.exe start LocalSignerService
```

#### Step 4: Verify Service is Running
```bash
Get-Service LocalSignerService
```

Expected output:
```
Status   Name               DisplayName
------   ----               -----------
Running  LocalSignerService Local DSC PDF Signer
```

#### Step 5: Verify Application is Listening
```bash
netstat -ano | findstr :9001
```

You should see the port 9001 in LISTENING state.

### Service Management Commands

**Start Service:**
```bash
nssm.exe start LocalSignerService
```

**Stop Service:**
```bash
nssm.exe stop LocalSignerService
```

**Restart Service:**
```bash
nssm.exe restart LocalSignerService
```

**Check Service Status:**
```bash
nssm.exe status LocalSignerService
```

**Remove Service:**
```bash
nssm.exe remove LocalSignerService confirm
```

**List All NSSM Services:**
```bash
nssm.exe list
```

### Troubleshooting Service Installation

If service doesn't appear after installation:

1. **Verify NSSM services:**
```bash
D:\Downloads\nssm-2.24\nssm-2.24\win64\nssm.exe list
```

2. **Check Windows Services:**
```bash
Get-Service | findstr Sign
```

3. **View Service Logs:**
Check the log files at:
- `D:\Downloads\local-signer\local-signer\target\service.log`
- `D:\Downloads\local-signer\local-signer\target\service-error.log`

4. **Common Issues:**
   - Ensure Java path is correct
   - Verify JAR file exists at specified location
   - Run NSSM as Administrator
   - Ensure DSC token drivers are installed
   - Check user account has permission to access DSC token

### Benefits of Running as Service

- ✅ Automatically starts on Windows boot
- ✅ Runs in background without user login
- ✅ No need to manually start application
- ✅ Survives system restarts
- ✅ Centralized service management
- ✅ Automatic logging and error handling

---

## 🌐 Network Usage

### Same System
Use:
```
http://localhost:9001
```

### Different System
1. Identify the IP address of the signer system
2. Attach the DSC token to that system
3. Call APIs using:
```
http://<SYSTEM_IP>:9001
```

---

## 🔐 Security Notes

- DSC private key never leaves the token.
- PIN is not stored in the application.
- All cryptographic operations are executed inside the DSC device.
- Signing process happens only on the local signer machine.

---

## 🛠 Use Cases

- Judicial document signing
- DSpace repository PDF signing
- Audit report signing
- Government document workflows
- Enterprise approval systems

---

## 👨‍💻 Developer

**Suryanshu Kumar**  
GitHub: [https://github.com/suryanshu-15](https://github.com/suryanshu-15)

---

## 📄 License

This project is intended for educational and enterprise integration purposes.  
Commercial usage requires appropriate authorization.

---

## 📬 Support

For issues, improvements, or feature requests, please raise an issue in the GitHub repository.

---

⭐ **If you find this project useful, please give it a star on GitHub!**
