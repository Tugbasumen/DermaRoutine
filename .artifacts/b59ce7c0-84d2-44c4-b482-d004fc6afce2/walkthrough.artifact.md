# Network Connectivity Fix Walkthrough

I have updated the project configuration to allow your physical Android device to connect to the backend server running on your computer.

## Changes Made

### 1. Backend: External Listening
- Updated `launchSettings.json` to use **`http://0.0.0.0:5222`**. This allows the .NET server to accept incoming connections from other devices on your Wi-Fi network.

### 2. Android: Real IP Integration
- Changed the `BASE_URL` in `NetworkModule.kt` from the emulator loopback (`10.0.2.2`) to your computer's actual network IP: **`192.168.1.103`**.
- Updated `network_security_config.xml` to grant permission for HTTP traffic to this new IP address.

## Final Steps to Test

> [!IMPORTANT]
> **Restart Required**:
> 1. Stop your running backend server in the terminal.
> 2. Start it again (e.g., `dotnet run`).
> 3. Ensure your phone is connected to the same Wi-Fi as the computer.
> 4. Run the Android app again.

The `Connection timed out` error should now be resolved, and your data should load correctly on your physical phone.
