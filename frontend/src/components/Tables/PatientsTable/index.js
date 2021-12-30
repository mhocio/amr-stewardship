import * as React from 'react';
import { Grid } from '@material-ui/core';
import { Typography } from '@mui/material';
import PeopleIcon from '@mui/icons-material/People';
import IconButton from '@mui/material/IconButton';
import InfoIcon from '@mui/icons-material/Info';
import { DataGrid, GridColDef, GridApi, GridCellValue } from '@mui/x-data-grid';

const columns = [
  { field: 'id', headerName: 'ID', width: 50 },
  { field: 'firstName', headerName: 'Imię', width: 100 },
  { field: 'lastName', headerName: 'Nazwisko', width: 100 },
  {
    field: 'Info',
    headerName: '',
    width: 30,
    sortable: false,
    renderCell: (params) => {
      const onClick = (e) => {
        e.stopPropagation(); // don't select this row after clicking

        const api = params.api;
        const thisRow= {};

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

const rows = [
  { id: 1, lastName: 'Snow', firstName: 'Jon' },
  { id: 2, lastName: 'Lannister', firstName: 'Cersei' },
  { id: 3, lastName: 'Lannister', firstName: 'Jaime' },
  { id: 4, lastName: 'Stark', firstName: 'Arya' },
  { id: 5, lastName: 'Targaryen', firstName: 'Daenerys' },
  { id: 6, lastName: 'Melisandre', firstName: 'Jarre', },
  { id: 7, lastName: 'Clifford', firstName: 'Ferrara' },
  { id: 8, lastName: 'Frances', firstName: 'Rossini' },
  { id: 9, lastName: 'Roxie', firstName: 'Harvey' },
];

export default function PatientsTable() {
  return (
    <div style={{ height: '84vh', width: '100%' }}>
      <Grid container direction="row" alignItems="center" spacing={1} wrap="nowrap">
        <Grid item>
          <PeopleIcon fontSize="large" />
        </Grid>
        <Grid item>
          <Typography variant="h5">Pacjenci</Typography>
        </Grid>
      </Grid>
      <DataGrid
        rows={rows}
        columns={columns}
        responsive={'scrollMaxHeight'}
        hideFooter={true}
      />
    </div>
  );
}
