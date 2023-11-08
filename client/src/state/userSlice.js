import { createSlice } from '@reduxjs/toolkit'

const initialState = {details:{}}

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    userLoggedIn( state, action){
        console.log("userLoggedIn", action);
        state.details = action.payload;
    }
  }
})

export const { userLoggedIn } = userSlice.actions

export const selectUserDetails = state => state.user.details

export default userSlice.reducer
