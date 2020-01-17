import React from 'react'
import {Switch, Route} from 'react-router-dom'

import Feed from './pages/Feed'
import New from './pages/New'
import Verify from './pages/Verify'

function Routes() {
    return (
        <Switch>
            <Route path="/" exact component={Feed} />
            <Route path="/new" exact component={New} />
            <Route path="/verify" exact component={Verify} />
        </Switch>
    )
}

export default Routes