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

  constructor() { }

  ngOnInit(): void {
  }

}
