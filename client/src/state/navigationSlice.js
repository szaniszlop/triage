import { createSlice } from '@reduxjs/toolkit'

const initialState = {location:""}

const navigationSlice = createSlice({
  name: 'navigation',
  initialState,
  reducers: {
    setNavigation( state, action){
        console.log("setNavigation", action);
        state.location = action.payload;
    },
    removeNavigation(state, action){
        console.log("removeNavigation", action);
        state.location = "";
    }
  }
})

export const { setNavigation, removeNavigation } = navigationSlice.actions

export const selectLocation = state => state.navigation.location

export default navigationSlice.reducer