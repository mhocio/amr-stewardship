import React from 'react'
import { useFormikContext, useField } from 'formik'
import * as Yup from 'yup';
import { Autocomplete } from '@mui/material';
import { TextField } from '@mui/material';

const AutocompleteWrapper = ({ name, options, ...props }) => {

  const { setFieldValue } = useFormikContext();
  const [field, meta] = useField(name);

  const handleChange = event => {
    const { value } = event.target;
    setFieldValue(name, value);
  }

  const configAutocomplete = {
    ...field,
    ...props,
    select: true,
    variant: 'outlined',
    fullWidth: true,
    onChange: handleChange
  }

  if (meta && meta.touched && meta.error) {
    configAutocomplete.error = true;
    configAutocomplete.helperText = meta.error;
  }


  return (
    <Autocomplete
      {...props}
      {...field}
      onChange={handleChange}
      value={[]}
      getOptionSelected={(item, current) => item.value === current.value}
      renderInput={configAutocomplete => (
        <TextField {...configAutocomplete}>
        </TextField>
      )}
    />
  )
}

export default AutocompleteWrapper;