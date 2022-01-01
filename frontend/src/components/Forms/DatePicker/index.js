import React from 'react';
import { TextField } from '@mui/material';
import { useField } from 'formik';

const DatePickerWrapper = (name,  props) => {

  const [field, meta] = useField(name);

  const configDatePicker = {
    ...field,
    props,
    type: 'date',
    variant: 'outlined',
    fullWidth: true,
    InputLabelProps: {
      shrink: true 
    }

  }

  if (meta && meta.error && meta.touched) {
    configDatePicker.error = true;
    configDatePicker.helperText = meta.error;
  }

  return (
    <TextField {...configDatePicker}/>
  )
}

export default DatePickerWrapper;