import * as React from 'react';

// material-ui
import { Box, Popper, Typography, Button, Fade, Paper } from '@mui/material';

export default function PositionedPopper() {

  <Button onClick={handleClick('bottom-end')}>bottom-end</Button>

  return (
    <Box sx={{ width: 500 }}>
      <Popper open={open} anchorEl={anchorEl} placement={placement} transition>
        {({ TransitionProps }) => (
          <Fade {...TransitionProps} timeout={350}>
            <Paper>
              <Typography sx={{ p: 2 }}>The content of the Popper.</Typography>
            </Paper>
          </Fade>
        )}
      </Popper>
      </Box>
  );
}
