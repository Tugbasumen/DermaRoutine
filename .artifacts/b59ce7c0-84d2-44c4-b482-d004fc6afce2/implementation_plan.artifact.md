# Network Connectivity Fix for Physical Devices

This plan addresses the `Connection timed out` error shown in your screenshot. The issue occurs because `10.0.2.2` only works for emulators; physical devices must use your computer's actual network IP address.

## User Action Required

> [!WARNING]
> **Local IP Address**: I detected your computer's IP as **`192.168.1.103`**. If you move to a different Wi-Fi network, this IP might change, and you will need to update the `BASE_URL` again.

## Proposed Changes

### 1. Backend: Allow External Connections

#### [MODIFY] [launchSettings.json](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Properties/launchSettings.json)
- Change `applicationUrl` from `http://localhost:5222` to **`http://0.0.0.0:5222`**.
- This tells the server to listen to requests from any device on your Wi-Fi, not just the computer itself.

### 2. Android: Update API Base URL

#### [MODIFY] [NetworkModule.kt](file:///C:/kotlinproje/DermaRoutine/app/src/main/java/com/dermaroutine/core/di/NetworkModule.kt)
- Change `BASE_URL` to **`http://192.168.1.103:5222/`**.

#### [MODIFY] [network_security_config.xml](file:///C:/kotlinproje/DermaRoutine/app/src/main/res/xml/network_security_config.xml)
- Add **`192.168.1.103`** to the permitted cleartext domains.

## Verification Plan

### Manual Verification
1. **Restart Backend**: Stop and start your .NET backend server.
2. **Connect Phone**: Ensure your phone is on the same Wi-Fi as your computer.
3. **Run App**: Open the app and check the "Rutin" page. The connection error should disappear, and data should load.
