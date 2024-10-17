import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import {FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule, FormsModule} from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHistory } from '../history.model';

@Component({
  standalone: true,
  selector: 'jhi-history-detail',
  templateUrl: './history-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ReactiveFormsModule, FormsModule],
})
export class HistoryDetailComponent implements OnInit{
  @Input() history: IHistory | null = null;

  editForm = this.fb.group({
    data: new FormArray([]),
    result: new FormArray([])
  })

  constructor(protected activatedRoute: ActivatedRoute,
              protected fb: FormBuilder) {}

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    console.log(JSON.parse(this.history?.predictionInput!)['close'].length);
    let numRows = JSON.parse(this.history?.predictionInput!)['close'].length;
    let data = JSON.parse(this.history?.predictionInput!);
    this.setRows(numRows);

    let numResultRows = JSON.parse(this.history?.predictionResult!)['close'].length;
    let result = JSON.parse(this.history?.predictionResult!);
    this.setResultRows(numResultRows)

    for (let i = 0; i < numRows; i++){
      this.dataArrays.at(i).patchValue({
        open: data['open'][i],
        high: data['high'][i],
        low: data['low'][i],
        close: data['close'][i],
        volume: data['volume'][i],
      })
    }

    for (let i = 0; i < numResultRows; i++){
      this.resultArrays.at(i).patchValue({
        open: result['open'][i],
        high: result['high'][i],
        low: result['low'][i],
        close: result['close'][i],
      })
    }
  }

  get f(): any {
    return this.editForm.controls;
  }

  get dataArrays():FormArray{
    return this.editForm.get('data') as FormArray;
  }

  get resultArrays():FormArray{
    return this.editForm.get('result') as FormArray;
  }

  getDataRows(): any {
    return this.f.data as FormArray;
  }

  getResultRows(): any {
    return this.f.result as FormArray;
  }

  addNewDataRow(): void {
    this.getDataRows().push(this.addDataRow());
  }

  addNewResultRow(): void {
    this.getResultRows().push(this.addResultRow());
  }

  addDataRow(): any {
    return this.fb.group({
      open: [{ value: null as number | null, disabled: true }, Validators.required],
      high: [{ value: null as number | null, disabled: true }, Validators.required],
      low: [{ value: null as number | null, disabled: true }, Validators.required],
      close: [{ value: null as number | null, disabled: true }, Validators.required],
      volume: [{ value: null as number | null, disabled: true }, Validators.required]
    });
  }

  addResultRow(): any {
    return this.fb.group({
      open: [{ value: null as number | null, disabled: true }, Validators.required],
      high: [{ value: null as number | null, disabled: true }, Validators.required],
      low: [{ value: null as number | null, disabled: true }, Validators.required],
      close: [{ value: null as number | null, disabled: true }, Validators.required]
    });
  }

  setRows(row: number): void {
    this.getDataRows().clear();
    for (let i = 0; i < row; i++){
      this.addNewDataRow();
    }
  }

  setResultRows(row: number): void {
    this.getResultRows().clear();
    for (let i = 0; i < row; i++){
      this.addNewResultRow();
    }
  }

}
