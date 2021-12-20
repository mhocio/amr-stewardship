import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Typography } from '@mui/material';
import TopicIcon from '@mui/icons-material/Topic';
import { Grid } from '@material-ui/core';

const columns = [
  { field: 'id', headerName: 'ID', width: 60 },
  { field: 'bacteria', headerName: 'Drobnoustrój', width: 130 },
  { field: 'antibiotic', headerName: 'Antybiotyk', width: 130 },
  { field: 'material', headerName: 'Materiał', width: 130 },
  { field: 'orderDate', headerName: 'Data zlecenia', width: 130 },
  { field: 'susceptibility', headerName: 'Wrażliwość', width: 130 }

];

const rows = [
  { id: 1, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 2, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 3, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 4, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 5, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 6, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 7, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 8, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 9, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 10, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 11, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 12, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 13, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },
  { id: 14, bacteria: 'Snow', antibiotic: 'Jon', material: 'Krew', orderDate: '20/10/2021', susceptibility: 'Wrażliwy' },

];

export default function DataTable() {
  return (
    <div style={{ height: 400, width: '100%' }}>
      <Grid container direction="row" alignItems="center" spacing={1} wrap="nowrap">
        <Grid item>
          <TopicIcon fontSize="large" />
        </Grid>
        <Grid item>
          <Typography variant="h5">Antybiogramy</Typography>
        </Grid>
      </Grid>
      <DataGrid
        rows={rows}
        columns={columns}
        responsive={'scrollMaxHeight'}
        hideFooter={true}
        rowHeight={30}
      />
    </div>
  );
}
