import React from 'react'

// material-ui
import { Avatar, Typography } from '@material-ui/core'
import { deepOrange } from '@mui/material/colors';
import { Grid } from '@material-ui/core';

export default function Account({ person }) {

    var initials = person.split(" ").map((x) => x[0]).join('');

    return (
        <Grid container spacing={2} alignItems="center" wrap="nowrap">
            <Grid item>
                <Avatar sx={{ bgcolor: deepOrange[400] }}>{initials}</Avatar>
            </Grid>
            <Grid item>
                <Typography noWrap style={{flex: 1} }>{person}</Typography>
            </Grid>
        </Grid>
    )
}
