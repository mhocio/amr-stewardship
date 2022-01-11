import React, { useEffect, useState } from 'react'
import { DataGrid } from '@mui/x-data-grid';
import { Typography, Grid, Paper, Skeleton, Box } from '@mui/material';
import TopicIcon from '@mui/icons-material/Topic';
import BASE_URL from '../../../constants/BASE_URL';

const columns = [
  { field: 'id', headerName: 'PESEL', width: 150 },
  { field: 'antibiogramId', headerName: 'ID antybiogramu', width: 150 },
  { field: 'ward', headerName: 'Oddział', width: 150 },
  { field: 'bacteria', headerName: 'Drobnoustrój', width: 200 },
  { field: 'antibiotic', headerName: 'Antybiotyk', width: 150 },
  { field: 'material', headerName: 'Materiał', width: 200 },
  { field: 'orderDate', headerName: 'Data zlecenia', width: 150 },
  { field: 'susceptibility', headerName: 'Wrażliwość', width: 100 }

];

export default function ExamTable({ data, loading }) {

  var skeletons = [];
  for (var i = 0; i < 12; i++) {
    skeletons.push(<Skeleton key={i} />)
  }

  return (
    <Paper sx={{ padding: '20px', paddingBottom: '70px' }}>
      <div style={{ height: 400, width: '100%' }}>
        <Grid container direction="row" alignItems="center" spacing={1} wrap="nowrap">
          <Grid item>
            <TopicIcon fontSize="large" />
          </Grid>
          <Grid item>
            <Typography variant="h5">Antybiogramy</Typography>
          </Grid>
        </Grid>
        {loading ?
          <Box sx={{ height: '100%' }}>
            <Skeleton height={70} />
            {skeletons}
          </Box>
          :
          <DataGrid
            rows={data}
            columns={columns}
            responsive={'scrollMaxHeight'}
            hideFooter={true}
            rowHeight={30}
          />
        }
      </div>
    </Paper>
  );
}
