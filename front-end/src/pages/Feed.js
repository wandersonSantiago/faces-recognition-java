import React, {Component} from 'react'
import './Feed.css'
import api from '../services/api'

class Feed extends Component {
    state = {
        feed: []
    }
    async componentDidMount() {
        const response = await api.get('person')
        this.setState({
            feed: response.data
        })
    }

    render(){
        return (
            <section id="post-list">
                {this.state.feed.map(person => (
                    <article key={person._id}>
                        <header>
                            <img src={`data:image/jpeg;base64,${person.photo}`} width="120"></img>
                            <div className="user-info">
                                <span>{person.name}</span>
                            </div>
                        </header>
                    </article>
                ))}
            </section>
        )
    }
}

export default Feed