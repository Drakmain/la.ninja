import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-market',
  templateUrl: './market.component.html',
  styleUrls: ['./market.component.css']
})
export class MarketComponent implements OnInit {

  market: String[] = [
    "Skin",
    "Engraving Recipe",
    "Enhancement Material",
    "Combat Supplies",
    "Cooking",
    "Trader",
    "Adventurer Tome",
    "Sailing",
    "Pets",
    "Mount",
    "Gem Chest"
  ];

  selectedSection!: String;

  constructor() { }

  ngOnInit(): void {
    console.log(this.selectedSection)
  }

  selectSection(id: String) {
    this.selectedSection = id.replace(' ', '_')
    console.log(this.selectedSection)
  }

}
