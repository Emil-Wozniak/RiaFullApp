import React, { Component } from 'react'

class Loading extends Component {
  render() {
    return (
     <React.Fragment>
     <img
     value="image"
     src={require("../images/Loading.gif")}
    //  style={{ width: "200px", height: "200px" }}
     alt=""
   />
     </React.Fragment>
    )
  }
}

export default Loading;