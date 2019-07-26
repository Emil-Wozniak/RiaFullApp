import React, {Component} from 'react'
import loading from "./Loading.gif"

class Loading extends Component {
    render() {
        return (
            <React.Fragment>
                <img
                    value="image"
                    src={loading}
                    style={{width: "200px", height: "200px"}}
                    alt=""
                />
            </React.Fragment>
        )
    }
}

export default Loading;
