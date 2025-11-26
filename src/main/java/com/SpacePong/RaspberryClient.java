// package com.SpacePong;

// import java.io.*;
// import java.net.*;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;

// public class RaspberryClient {
//     private String serverUrl;
//     private int serverPort;
//     private String clientId;
    
//     public static void main(String[] args) {
//         System.out.println("🚀 Iniciando Raspberry Pi Client - SpacePong");
//         new RaspberryClient().start();
//     }
    
//     public RaspberryClient() {
//         loadConnectionConfig();
//     }
    
//     private void loadConnectionConfig() {
//         try {
//             String configContent = new String(Files.readAllBytes(Paths.get("config/connection.json")));
//             JsonObject config = JsonParser.parseString(configContent).getAsJsonObject();
            
//             this.serverUrl = config.get("server_url").getAsString();
//             this.serverPort = config.get("server_port").getAsInt();
//             this.clientId = config.get("client_id").getAsString();
            
//             System.out.println("✅ Configuración de conexión cargada:");
//             System.out.println("   📍 Servidor: " + serverUrl + ":" + serverPort);
//             System.out.println("   🆔 Client ID: " + clientId);
            
//         } catch (Exception e) {
//             System.err.println("❌ Error cargando configuración: " + e.getMessage());
//             this.serverUrl = "localhost";
//             this.serverPort = 3000;
//             this.clientId = "rpi_default";
//             System.out.println("⚙️  Usando configuración por defecto");
//         }
//     }
    
//     public void start() {
//         while (true) {
//             try {
//                 System.out.println("🔗 Conectando al servidor " + serverUrl + ":" + serverPort + "...");
                
//                 Socket socket = new Socket(serverUrl, serverPort);
//                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
//                 System.out.println("✅ Conectado al servidor!");
                
//                 // Enviar mensaje de registro
//                 String registerMessage = createRegisterMessage();
//                 out.println(registerMessage);
//                 System.out.println("📤 Mensaje enviado: " + registerMessage);
                
//                 // Leer respuestas del servidor
//                 String response;
//                 while ((response = in.readLine()) != null) {
//                     System.out.println("📨 Respuesta del servidor: " + response);
//                     processServerResponse(response);
//                 }
                
//                 System.out.println("🔌 Servidor cerró la conexión");
//                 socket.close();
                
//             } catch (IOException e) {
//                 System.err.println("❌ Error de conexión: " + e.getMessage());
//             }
            
//             // Esperar antes de reconectar
//             System.out.println("🔄 Intentando reconectar en 5 segundos...");
//             try {
//                 Thread.sleep(5000);
//             } catch (InterruptedException e) {
//                 break;
//             }
//         }
//     }
    
//     private String createRegisterMessage() {
//         JsonObject message = new JsonObject();
//         message.addProperty("type", "register");
//         message.addProperty("client_id", clientId);
//         message.addProperty("timestamp", System.currentTimeMillis());
//         return message.toString();
//     }
    
//     private void processServerResponse(String response) {
//         try {
//             JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
//             String type = jsonResponse.get("type").getAsString();
            
//             switch (type) {
//                 case "acceptRegister":
//                     System.out.println("🎉 Registro aceptado en el servidor");
//                     break;
                    
//                 case "configuration":
//                     // Procesar el JSON SpacePong recibido del servidor
//                     processSpacePongConfiguration(jsonResponse);
//                     break;
                    
//                 default:
//                     System.out.println("📨 Mensaje del servidor: " + type);
//             }
//         } catch (Exception e) {
//             System.out.println("📨 Respuesta del servidor: " + response);
//         }
//     }
    
//     private void processSpacePongConfiguration(JsonObject configMessage) {
//         try {
//             JsonObject data = configMessage.get("data").getAsJsonObject();
            
//             // Extraer los datos del JSON SpacePong
//             String name = data.get("name").getAsString();
//             String message = data.has("message") ? data.get("message").getAsString() : "";
//             String version = data.has("version") ? data.get("version").getAsString() : "1.0";
            
//             System.out.println("⚙️  Configuración SpacePong recibida:");
//             System.out.println("   🎮 Nombre: " + name);
//             System.out.println("   📝 Mensaje: " + message);
//             System.out.println("   🔢 Versión: " + version);
            
//             // Mostrar en display
//             displaySpacePongInfo(name, message, version);
            
//         } catch (Exception e) {
//             System.err.println("❌ Error procesando configuración SpacePong: " + e.getMessage());
//             System.err.println("📋 JSON recibido: " + configMessage.toString());
//         }
//     }
    
//     private void displaySpacePongInfo(String name, String message, String version) {
//         // Simulación de display - En RPi real usarías librerías de display OLED
//         System.out.println("=".repeat(40));
//         System.out.println("📺 DISPLAY SPACEPONG");
//         System.out.println("=".repeat(40));
//         System.out.println(" 🎮 " + name);
//         System.out.println(" 📝 " + message);
//         System.out.println(" 🔢 v" + version);
//         System.out.println(" ⏰ " + java.time.LocalTime.now());
//         System.out.println("=".repeat(40));
        
//         // Guardar en archivo para simular display (en RPi real sería display físico)
//         try {
//             String displayContent = 
//                 "=== SPACEPONG ===\n" +
//                 "Nombre: " + name + "\n" +
//                 "Mensaje: " + message + "\n" +
//                 "Version: " + version + "\n" +
//                 "Hora: " + java.time.LocalTime.now() + "\n" +
//                 "==================";
            
//             Files.write(Paths.get("display_output.txt"), displayContent.getBytes());
//             System.out.println("💾 Información guardada en display_output.txt");
            
//         } catch (IOException e) {
//             System.err.println("❌ Error guardando en display: " + e.getMessage());
//         }
//     }
// }   