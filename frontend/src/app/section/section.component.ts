import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import axios from 'axios';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.css']
})
export class SectionComponent implements OnInit {

  @Input() section!: String;

  itemNames!: Array<String>;

  constructor() { }

  ngOnInit(): void {
  }

  async ngOnChanges(changes: SimpleChanges) {
    if (this.section != undefined) {
      var { data: response } = await axios.get(
        'http://localhost:8080/backend_war_exploded/api/market/' + this.section + '/names',
        {
          headers: {
            'Content-Type': 'application/json'
          },
        }
      );

      this.itemNames = Object.values(response);
      this.itemNames.pop();
      this.itemNames.pop();
    }
  }
}
