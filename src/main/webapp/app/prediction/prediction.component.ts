import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule, FormsModule} from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import {PredictionService} from 'app/prediction/service/prediction.service';
import accountRoute from 'app/account/account.route';
import {AccountService} from 'app/core/auth/account.service';
import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {Account} from 'app/core/auth/account.model';
import {HistoryService} from 'app/entities/history/service/history.service';
import {IHistory} from 'app/entities/history/history.model';
@Component({
  selector: 'jhi-prediction',
  standalone: true,
  imports: [ReactiveFormsModule, SharedModule, FormsModule],
  templateUrl: './prediction.component.html',
  styleUrl: './prediction.component.scss'
})
export class PredictionComponent implements OnInit{
  windowSize = [
    {value: 3, text: '3'},
    // {value: 7, text: '7'}
  ]
  predictionDay = [
    {value: 1, text: '1'},
    {value: 3, text: '3'}
  ]
  prediction?: number[][];
  users?: IUser[];
  account?: Account;
  history?: IHistory;
  editForm = this.fb.group({
    windowSize: [null as number | null],
    predictionDay: [null as number | null],
    data: new FormArray([])
  })
  constructor(private fb: FormBuilder,
              protected predictionService: PredictionService,
              protected userService: UserService,
              protected accountService: AccountService,
              protected historyService: HistoryService) {}

  ngOnInit(): void {
    this.userService.query().subscribe(res => this.users = res.body!);
    this.accountService.identity().subscribe(account => this.account = account!);
  }

  get f(): any {
    return this.editForm.controls;
  }

  getDataRows(): any {
    return this.f.data as FormArray;
  }

  addNewDataRow(): void {
    this.getDataRows().push(this.addDataRow());
  }

  addDataRow(): any {
    return this.fb.group({
      open: [null as number | null, Validators.required],
      high: [null as number | null, Validators.required],
      low: [null as number | null, Validators.required],
      close: [null as number | null, Validators.required],
      volume: [null as number | null, Validators.required]
    });
  }

  setRows(event: any): any {
    this.getDataRows().clear();
    if (event.value === 3){
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
    }
    if (event.value === 7){
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
      this.addNewDataRow();
    }
  }

  sent(): any{
    const rawData = this.editForm.getRawValue().data as { open: number; high: number; low: number; close: number; volume: number }[];
    const data = {
      open: [] as number[],
      high: [] as number[],
      low: [] as number[],
      close: [] as number[],
      volume: [] as number[]
    };
    rawData.forEach(item => {
      data.open.push(item.open);
      data.high.push(item.high);
      data.low.push(item.low);
      data.close.push(item.close);
      data.volume.push(item.volume);
    })

    const payload = {
      data: data,
      windowSize: this.editForm.getRawValue().windowSize,
      predictionDay: this.editForm.getRawValue().predictionDay
    }
    this.predictionService.getPrediction(payload).subscribe(res => this.prediction = res.prediction);
  }

  savePrediction(): any {
    const rawData = this.editForm.getRawValue().data as { open: number; high: number; low: number; close: number; volume: number }[];
    const data = {
      open: [] as number[],
      high: [] as number[],
      low: [] as number[],
      close: [] as number[],
      volume: [] as number[]
    };
    rawData.forEach(item => {
      data.open.push(item.open);
      data.high.push(item.high);
      data.low.push(item.low);
      data.close.push(item.close);
      data.volume.push(item.volume);
    })

    const payload = {
      id: null,
      predictionInput: JSON.stringify(data),
      predictionResult: JSON.stringify(data),
      windowSize: this.editForm.getRawValue().windowSize,
      predictionDay: this.editForm.getRawValue().predictionDay,
      user: this.users?.filter(e => e.login == this.account?.login)[0]
    }

    console.log(this.history)
    this.historyService.create(payload).subscribe();
  }
}
