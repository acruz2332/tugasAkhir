import { Component } from '@angular/core';
import {CandlestickChartComponent} from 'app/entities/dashboard/candlestick-chart/candlestick-chart.component';

@Component({
  selector: 'jhi-dashboard',
  standalone: true,
  imports: [CandlestickChartComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

}
