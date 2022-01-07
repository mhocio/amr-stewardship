import React from 'react';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';

// material-ui
import { Button, Grid } from '@mui/material';

// project imports
import MyDatePicker from '../Forms/DatePicker'
import AnalyzeButton from '../Forms/Button';
import MySelect from '../Forms/Select'

// dummy data
import materials from '../../data/materials.json'
import wards from '../../data/wards.json'

const initialValues = {
  startDate: '',
  endDate: '',
  material: '',
  ward: ''
}

const validationSchema = Yup.object({
  startDate: Yup.date()
    .required("Data początkowa jest wymagana"),
  endDate: Yup.date()
    .when(
      "startDate",
      (startDate, schema) => startDate && schema.min(startDate,
         "Data końcowa nie może poprzedzać początkowej"))
    .required("Data końcowa jest wymagana"),
  material: Yup.string()
    .required('Badany materiał jest wymagany'),
  ward: Yup.string()
    .required('Oddział jest wymagany')
})

export default function FratMenu() {

  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      validateOnBlur={false}
      validateOnChange
      onSubmit={values => {
        console.log(values)
      }}>
      <Form>
        <Grid container direction="column" spacing={1} wrap="nowrap">
          <Grid container direction="row" spacing={1} wrap="nowrap" justifyContent="flex-start" alignItems="center">
            <Grid item xs={2}>
              <MyDatePicker
                name="startDate"
                label="Od"
              />
            </Grid>
            <Grid item xs={2}>
              <MyDatePicker
                name="endDate"
                label="Do"
              />
            </Grid>
            <Grid container item xs={2} justifyContent="flex-end" >
              <MySelect
                name="ward"
                label="Oddział"
                options={wards}
              />
            </Grid>
            <Grid container item xs={2} justifyContent="flex-end">
              <MySelect
                name="material"
                label="Badany materiał"
                options={materials}
              />
            </Grid>
            <Grid container item xs={4} justifyContent="flex-end">
              <AnalyzeButton>
                Analizuj
              </AnalyzeButton>
            </Grid>
          </Grid>
          <Grid container item wrap="nowrap" justifyContent="flex-start" spacing={1} style={{ paddingLeft: 0 }}>
            <Grid item>
              <Button variant="outlined" size="small">
                12 miesięcy
              </Button>
            </Grid>
            <Grid item>
              <Button variant="outlined" size="small">
                6 miesięcy
              </Button>
            </Grid>
            <Grid item>
              <Button variant="outlined" size="small">
                3 miesiące
              </Button>
            </Grid>
          </Grid>
        </Grid>
      </Form>
    </Formik>
  );
}
