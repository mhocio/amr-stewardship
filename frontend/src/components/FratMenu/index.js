import React, { useState, useEffect } from 'react';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import * as dayjs from 'dayjs'

// material-ui
import { Button, Grid } from '@mui/material';

// project imports
import MyDatePicker from '../Forms/DatePicker'
import AnalyzeButton from '../Forms/Button';
import MySelect from '../Forms/Select'

import BASE_URL from '../../constants/BASE_URL';

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

  const [wards, setWards] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [errorFlag, setErrorFlag] = useState(false);
  const [loading, setLoading] = useState(true);
  const [dateFrom, setDateFrom] = useState('');
  const [dateTo, setDateTo] = useState('');

  useEffect(() => {
    getDropdownData();
  }, [])

  const today = dayjs();

  const getDropdownData = async () => {
    setLoading(true);
    Promise.all([
      await fetch(`${BASE_URL}/wards`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json'
          // 'Authorization': 'Bearer ' + authToken
        }
      }),
      await fetch(`${BASE_URL}/materials`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json'
          // 'Authorization': 'Bearer ' + authToken
        }
      })
    ])
      .then(([res1, res2]) => Promise.all([res1.json(), res2.json()]))
      .then(([data1, data2]) => {
        setWards(data1);
        setMaterials(data2);
      })
      .catch((err) => {
        setErrorFlag(true);
        console.log(`Couldn't fetch wards or materials: ${err}`);
      })
      .finally(() => {
        setLoading(false);
      })
  }

  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      validateOnBlur={false}
      validateOnChange
      onSubmit={values => {
        console.log(values)
      }}>
      {({ isSubmitting, dirty, setFieldValue }) => (
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
                <Button variant="outlined" size="small" onClick={() => { setFieldValue('startDate', today.subtract('12', 'month').format("YYYY-MM-DD")); setFieldValue('endDate', today.format("YYYY-MM-DD")); }}>
                  12 miesięcy
                </Button>
              </Grid>
              <Grid item>
                <Button variant="outlined" size="small" onClick={() => { setFieldValue('startDate', today.subtract('6', 'month').format("YYYY-MM-DD")); setFieldValue('endDate', today.format("YYYY-MM-DD")); }}>
                  6 miesięcy
                </Button>
              </Grid>
              <Grid item>
                <Button variant="outlined" size="small" onClick={() => { setFieldValue('startDate', today.subtract('3', 'month').format("YYYY-MM-DD")); setFieldValue('endDate', today.format("YYYY-MM-DD")); }}>
                  3 miesiące
                </Button>
              </Grid>
            </Grid>
          </Grid>
        </Form>
      )}
    </Formik>
  );
}
