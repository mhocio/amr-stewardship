import React from 'react'

// material-ui
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';

// project imports
import BasicTabs from '../components/BasicTabs'
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";

const TrendsPage = () => {
  return (
    <>
      <DrawerHeader />
      <BasicTabs />
    </>

  )
}

export default TrendsPage
