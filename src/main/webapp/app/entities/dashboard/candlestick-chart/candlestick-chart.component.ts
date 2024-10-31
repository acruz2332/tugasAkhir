import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock'; // Import Highcharts Stock for candlestick
import { HighchartsChartModule } from 'highcharts-angular';
import {PredictionService} from 'app/prediction/service/prediction.service';

@Component({
  selector: 'app-candlestick-chart',
  standalone: true,
  imports: [
    HighchartsChartModule
  ],
  templateUrl: './candlestick-chart.component.html'
})
export class CandlestickChartComponent implements OnInit{
  updateFlag = false;
  constructor(protected predictionService: PredictionService) {}
  Highcharts: typeof Highcharts = Highcharts;
  chartOptions: Highcharts.Options = {
    chart: {
      type: 'candlestick',
      height: 600,
      backgroundColor: '#f4f4f4', // Background color
      plotBorderWidth: 1,
      zooming: {
        type: 'xy'
      }
    },
    title: {
      text: 'Bitcoin Price',
      style: {
        color: '#333',
        fontSize: '20px',
        fontWeight: 'bold'
      }
    },
    xAxis: {
      type: 'datetime', // Ensure datetime format for x-axis
      labels: {
        format: '{value:%Y-%m-%d}', // Date format
        style: {
          color: '#000',
          fontSize: '12px'
        }
      },
      minRange: 24 * 3600 * 1000, // One day
      range: 7 * 24 * 3600 * 1000, // One week
      gridLineWidth: 1
    },
    yAxis: {
      title: {
        text: 'Price (USD)',
        style: {
          color: '#000'
        }
      },
      gridLineWidth: 1
    },
    tooltip: {
      split: true,
      valueDecimals: 2,
      xDateFormat: '%Y-%m-%d'
    },
    series: [{
      type: 'candlestick',
      name: 'BTC to USD',
      data: this.getCandlestickData(),
      color: '#d32f2f', // Color for bearish candlesticks
      upColor: '#388e3c', // Color for bullish candlesticks
      lineColor: '#333',
      tooltip: {
        valueDecimals: 2
      }
    }],
    rangeSelector: {
      enabled: true,
      buttons: [{
        type: 'day',
        count: 7,
        text: '1w'
      }, {
        type: 'month',
        count: 1,
        text: '1m'
      }, {
        type: 'month',
        count: 3,
        text: '3m'
      }, {
        type: 'month',
        count: 6,
        text: '6m'
      }, {
        type: 'year',
        count: 1,
        text: '1y'
      }, {
        type: 'all',
        text: 'All'
      }],
      selected: 0
    }
  };

  ngOnInit(): void{
  }

  getCandlestickData() {
    this.predictionService.getAllData().subscribe(res => {
      const chartData: any[] = [];
      for (let i = 0; i < res.c.length; i++){
        chartData.push([Date.UTC(res.y[i], res.m[i] - 1, res.d[i]), res.o[i], res.h[i], res.l[i], res.c[i]])
      }
      // Update the series data
      if (this.chartOptions.series?.[0]) {
        (this.chartOptions.series[0] as any).data = chartData;
      }
      // Trigger chart update
      this.updateFlag = true;
    });

    // Return an empty array or default data initially if necessary
    return [];
  }


}
