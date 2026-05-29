import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationService, Notification } from './services/notification';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {

  notifications: Notification[] = [];
  newMessage: string = '';
  statusMessage: string = '';

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.notificationService.getNotifications().subscribe({
      next: (data) => this.notifications = data,
      error: (err) => console.error('Error cargando notificaciones', err)
    });
  }

  sendNotification(): void {
    if (!this.newMessage.trim()) return;
    this.notificationService.sendNotification(this.newMessage).subscribe({
      next: () => {
        this.statusMessage = 'Notificación enviada correctamente';
        this.newMessage = '';
        setTimeout(() => this.loadNotifications(), 1000);
      },
      error: (err) => console.error('Error enviando notificación', err)
    });
  }
}