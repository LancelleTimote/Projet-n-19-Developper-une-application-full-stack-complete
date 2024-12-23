import { Component, OnInit } from '@angular/core';
import { TopicsService } from '../../services/topics.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  topics: any[] = [];

  constructor(private topicsService: TopicsService) {}

  ngOnInit(): void {
    this.topicsService.getTopics().subscribe(
      (data) => {
        this.topics = data;
      },
      (error) => {
        console.error('Erreur de récupération des topics', error);
      }
    );
  }

  subscribe(topicId: number): void {
    this.topicsService.subscribeToTopic(topicId).subscribe(
      (response) => {
        console.log('Abonnement réussi :', response); // Ajoutez un log pour voir la réponse
      },
      (error) => {
        console.error("Erreur lors de l'abonnement", error);
      }
    );
  }
}
