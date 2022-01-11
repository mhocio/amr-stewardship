import React, { useEffect, useState } from 'react'
import { Grid } from '@material-ui/core';
import { Typography, Skeleton, Box } from '@mui/material';
import PeopleIcon from '@mui/icons-material/People';
import IconButton from '@mui/material/IconButton';
import InfoIcon from '@mui/icons-material/Info';
import { DataGrid, GridColDef, GridApi, GridCellValue } from '@mui/x-data-grid';
import { Paper } from '@mui/material';
import axios from 'axios';
import BASE_URL from '../../../constants/BASE_URL';
import authHeader from '../../../services/auth-header';

const columns = [
  { field: 'id', headerName: 'PESEL', width: 120 },
  { field: 'firstName', headerName: 'Imię', width: 100 },
  { field: 'secondName', headerName: 'Nazwisko', width: 100 },
  {
    field: 'Info',
    headerName: '',
    width: 30,
    sortable: false,
    renderCell: (params) => {
      const onClick = (e) => {
        e.stopPropagation(); // don't select this row after clicking

        const api = params.api;
        const thisRow = {};

        api
          .getAllColumns()
          .filter((c) => c.field !== '__check__' && !!c)
          .forEach(
            (c) => (thisRow[c.field] = params.getValue(params.id, c.field)),
          );

        return alert(JSON.stringify(thisRow, null, 4));
      };

      return <IconButton onClick={onClick}><InfoIcon /></IconButton>;
    },
  },
];


export default function PatientsTable({ data, loading, handlePatientAntibiograms }) {

  var skeletons = [];
  for (var i = 0; i < 12; i++) {
    skeletons.push(<Skeleton sx= {{ height: 40 }} key={i} />)
  }

  const handleOnRowClick = (params) => {
    axios.get(`${BASE_URL}/antibiogram/by-pesel/${params.row.pesel}`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          ...authHeader()
        }
    })
    .then(res => {
      res.data.map((e) => { e['id'] = e.antibiogramId; });
      handlePatientAntibiograms(res.data);
    })
  }

  return (
    <Paper sx={{ padding: '20px', paddingBottom: '70px' }}>
      <div style={{ height: '79vh', width: '100%' }}>
        <Grid container direction="row" alignItems="center" spacing={1} wrap="nowrap">
          <Grid item>
            <PeopleIcon fontSize="large" />
          </Grid>
          <Grid item>
            <Typography variant="h5">Pacjenci</Typography>
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
            onRowClick={handleOnRowClick}
          />
        }
      </div>
    </Paper>
  );
}
