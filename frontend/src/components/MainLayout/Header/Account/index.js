import React from 'react'

// material-ui
import { Avatar, Typography } from '@material-ui/core'
import { deepOrange } from '@mui/material/colors';
import { Grid } from '@material-ui/core';
import { Chip } from '@mui/material';

export default function Account({ person }) {

    var initials = person.split(" ").map((x) => x[0]).join('');

    return (
        <Chip avatar={<Avatar>{initials}</Avatar>} label={person} color="secondary"/>
    )
}
