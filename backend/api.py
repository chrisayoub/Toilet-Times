from db import User, Post, getSession
from flask import Flask, request
from flask_restful import Resource, Api
from flask_restful import Resource, fields, marshal
from sqlalchemy import ForeignKey, desc
import json
import datetime
from datetime import timedelta

app = Flask("Toilet Times")
api = Api(app)

lim_val = 10

post_fields = {
    'id':   fields.Integer,
   	'userId': fields.Integer,
    'rating':     fields.Integer,
    'comment': fields.String,
    'time': fields.DateTime(dt_format='iso8601'),
    'voteTotal': fields.Integer,
    'flagCount': fields.Integer,
    'building': fields.String,
    'floor': fields.Integer,
    'locationDetailText': fields.String
}

# Endpoints

# Add user (POST)
class NewUser(Resource):
	def post(self):
		user = User()
		session = getSession()
		session.add(user)
		session.commit()
		return {'id' : user.id}

# Get user posts (GET)
class UserPosts(Resource):
	def get(self):
		id = request.args['id']
		session = getSession()
		return {'posts': [marshal(p, post_fields) for p in session.query(Post).filter_by(userId=id).all()]}
			
# Make a new post (POST)
class NewPost(Resource):
	def post(self):
		userId = request.form['userId']
		rating = request.form['rating']
		comment = request.form['comment']
		time = request.form['time']
		voteTotal = request.form['voteTotal']
		flagCount = request.form['flagCount']
		building = request.form['building']
		floor = request.form['floor']
		locationDetailText = request.form['locationDetailText']
		post = Post(userId = userId, rating = rating, comment = comment, time = time, voteTotal = voteTotal, flagCount = flagCount, building = building, floor = floor, locationDetailText = locationDetailText)
		session = getSession()
		session.add(post)
		session.commit()
		return 'OK'

# Get trending posts <- paginated, send page number (GET)
class TrendingPosts(Resource):
	def get(self):
		page = request.args['page']
		now = datetime.datetime.now()
		dayAgo = now - timedelta(days = 1)
		session = getSession()
		return {'posts': [marshal(p, post_fields) for p in session.query(Post).filter(Post.time >= dayAgo).limit(lim_val).offset(lim_val * page).all()]}

# Get all time posts <- paginated, send page number (GET)
class AllTimePosts(Resource):
	def get(self):
		page = request.args['page']
		session = getSession()
		return {'posts': [marshal(p, post_fields) for p in session.query(Post).order_by(desc(Post.voteTotal)).limit(lim_val).offset(lim_val * page).all()]}

# Get recent posts <- paginated, send page number (GET)
class RecentPosts(Resource):
	def get(self):
		page = request.args['page']
		session = getSession()
		return {'posts': [marshal(p, post_fields) for p in session.query(Post).order_by(desc(Post.time)).limit(lim_val).offset(lim_val * page).all()]}

# Flag post as inappropriate (POST)
class Inappropriate(Resource):
	def post(self):
		session = getSession()
		id = request.form['id']
		post = session.query(Post).filter_by(id=id)[0]
		post.flagCount += 1
		session.commit()
		return 'OK'

# Upvote or downvote a post (POST)
class UpvotePost(Resource):
	def post(self):
		session = getSession()
		id = request.form['id']
		post = session.query(Post).filter_by(id=id)[0]
		post.voteTotal += 1
		session.commit()
		return 'OK'

class DownvotePost(Resource):
	def post(self):
		session = getSession()
		id = request.form['id']
		post = session.query(Post).filter_by(id=id)[0]
		post.voteTotal -= 1
		session.commit()
		return 'OK'

# Delete a post (POST)
class DeletePost(Resource):
	def post(self):
		session = getSession()
		id = request.form['id']
		post = session.query(Post).filter_by(id=id)[0]
		session.delete(post)
		session.commit()
		return 'OK'

api.add_resource(NewUser, '/user/new')
api.add_resource(UserPosts, '/user/posts')
api.add_resource(NewPost, '/post/new')
api.add_resource(TrendingPosts, '/posts/trending')
api.add_resource(AllTimePosts, '/posts/all')
api.add_resource(RecentPosts, '/posts/recent')
api.add_resource(Inappropriate, '/post/flag')
api.add_resource(UpvotePost, '/post/upvote')
api.add_resource(DownvotePost, '/post/downvote')
api.add_resource(DeletePost, '/post/delete')

if __name__ == '__main__':
	app.run(debug=True, host='0.0.0.0', port=443)