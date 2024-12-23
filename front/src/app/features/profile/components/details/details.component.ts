import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DetailsService } from '../../services/details.service'; // Import du service

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  subscriptions: {
    topicId: number;
    topicTitle: string;
    topicDescription: string;
  }[] = [];

  constructor(private detailsService: DetailsService, private router: Router) {}

  ngOnInit(): void {
    this.loadSubscriptions();
  }

  loadSubscriptions(): void {
    this.detailsService.getUserSubscriptions().subscribe({
      next: (data) => {
        console.log('Données des abonnements reçues :', data);
        this.subscriptions = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des abonnements:', err);
      },
    });
  }

  unsubscribe(topicId: number): void {
    this.detailsService.unsubscribe(topicId).subscribe({
      next: () => {
        this.subscriptions = this.subscriptions.filter(
          (sub) => sub.topicId !== topicId
        );
      },
      error: (err) => {
        console.error('Erreur lors de la désinscription:', err);
      },
    });
  }

  // logOut(): void {
  //   this.sessionService.logOut();
  //   this.router.navigate(['/login']);
  // }
}
