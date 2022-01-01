import React from 'react';
import { LoadingButton } from '@mui/lab';
import { useFormikContext } from 'formik';
import CalculateIcon from '@mui/icons-material/Calculate';

const ButtonWrapper = ({ children, ...otherProps }) => {

  const { submitForm } = useFormikContext();
  const [loading, setLoading] = React.useState(false);

  const handleSubmit = () => {
    submitForm();
    setLoading(true);
  }

  const configButton = {
    variant: 'contained',
    onClick: handleSubmit,
    color: 'primary',
    size: 'large',
    loadingPosition: 'end',
  }

  return (
    <LoadingButton {...configButton}
    endIcon={<CalculateIcon />}
    loading={loading}>
      {children}
    </LoadingButton>
  );
};

export default ButtonWrapper;