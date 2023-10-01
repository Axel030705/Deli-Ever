package Vendedor;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.agenda.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Maneja las notificaciones FCM recibidas aquí
        if (remoteMessage.getData().size() > 0) {
            // Extrae datos de la notificación
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            // Puedes mostrar la notificación o realizar otras acciones aquí
            mostrarNotificacion(title, body);
        }
    }

    @SuppressLint("MissingPermission")
    private void mostrarNotificacion(String title, String body) {
        // Aquí hay un ejemplo básico:
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "canal_notificaciones")
                .setSmallIcon(R.drawable.icono_correo)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Enviar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}



