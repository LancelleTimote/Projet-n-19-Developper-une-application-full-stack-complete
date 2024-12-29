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
      (topics) => {
        this.topicsService.getUserSubscriptions().subscribe(
          (subscriptions) => {
            this.topics = topics.map((topic) => ({
              ...topic,
              isSubscribed: subscriptions.includes(topic.id),
            }));
          },
          (error) => {
            console.error('Erreur de récupération des abonnements', error);
            this.topics = topics.map((topic) => ({
              ...topic,
              isSubscribed: false,
            }));
          }
        );
      },
      (error) => {
        console.error('Erreur de récupération des topics', error);
      }
    );
  }

  subscribe(topicId: number): void {
    const topic = this.topics.find((t) => t.id === topicId);
    if (!topic) return;

    this.topicsService.subscribeToTopic(topicId).subscribe(
      (response) => {
        console.log('Abonnement réussi :', response);
        topic.isSubscribed = true;
      },
      (error) => {
        console.error("Erreur lors de l'abonnement", error);
      }
    );
  }
}
