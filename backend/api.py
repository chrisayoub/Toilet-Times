from db import User, Post, getSession, Vote
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
    'floor': fields.Integer
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
		session = getSession()
		userId = request.args['id']

		now = datetime.datetime.now()
		dayAgo = now - timedelta(days = 1)

		listPosts = [marshal(p, post_fields) for p in session.query(Post).filter_by(userId=id).order_by(desc(Post.time)).all()]
		for postDict in listPosts:
			postId = postDict['id']
			votes = session.query(Vote).filter_by(userId=userId, postId=postId).all()

			if len(votes) != 0:
				vote = votes[0]
				postDict['userVote'] = vote.vote
			else:
				postDict['userVote'] = 0

		return {'posts': listPosts}
			
# Make a new post (POST)
class NewPost(Resource):
	def post(self):
		userId = request.form['userId']
		rating = request.form['rating']
		comment = request.form['comment']
		building = request.form['building']
		floor = request.form['floor']
		time = datetime.datetime.now()
		post = Post(userId = userId, rating = rating, comment = comment, time = time, voteTotal = 0, flagCount = 0, building = building, floor = floor)
		session = getSession()
		session.add(post)
		session.commit()
		return 'OK'

# Get trending posts <- paginated, send page number (GET)
class TrendingPosts(Resource):
	def get(self):
		session = getSession()
		page = int(request.args['page'])
		userId = request.args['userId']

		now = datetime.datetime.now()
		dayAgo = now - timedelta(days = 1)

		listPosts = [marshal(p, post_fields) for p in session.query(Post).filter(Post.time >= dayAgo).limit(lim_val).offset(lim_val * page).all()]
		for postDict in listPosts:
			postId = postDict['id']
			votes = session.query(Vote).filter_by(userId=userId, postId=postId).all()

			if len(votes) != 0:
				vote = votes[0]
				postDict['userVote'] = vote.vote
			else:
				postDict['userVote'] = 0

		return {'posts': listPosts}

# Get all time posts <- paginated, send page number (GET)
class AllTimePosts(Resource):
	def get(self):
		session = getSession()
		page = int(request.args['page'])
		userId = request.args['userId']

		listPosts = [marshal(p, post_fields) for p in session.query(Post).order_by(desc(Post.voteTotal)).offset(lim_val * page).limit(lim_val).all()]
		for postDict in listPosts:
			postId = postDict['id']
			votes = session.query(Vote).filter_by(userId=userId, postId=postId).all()
			
			if len(votes) != 0:
				vote = votes[0]
				postDict['userVote'] = vote.vote
			else:
				postDict['userVote'] = 0

		return {'posts': listPosts}

# Get recent posts <- paginated, send page number (GET)
class RecentPosts(Resource):
	def get(self):
		session = getSession()
		page = int(request.args['page'])
		userId = request.args['userId']

		listPosts = [marshal(p, post_fields) for p in session.query(Post).order_by(desc(Post.time)).limit(lim_val).offset(lim_val * page).all()]
		for postDict in listPosts:
			postId = postDict['id']
			votes = session.query(Vote).filter_by(userId=userId, postId=postId).all()

			if len(votes) != 0:
				vote = votes[0]
				postDict['userVote'] = vote.vote
			else:
				postDict['userVote'] = 0

		return {'posts': listPosts}

# Flag post as inappropriate (POST)
class Inappropriate(Resource):
	def post(self):
		session = getSession()
		id = request.form['id']
		post = session.query(Post).filter_by(id=id)[0]
		post.flagCount += 1
		session.commit()
		return 'OK'

# Vote a post (POST)
class VotePost(Resource):
	def post(self):
		postId = request.form['postId']
		userId = request.form['userId']
		voteValue = int(request.form['value'])

		session = getSession()
		user = session.query(User).filter_by(id=userId).all()[0]
		post = session.query(Post).filter_by(id=postId).all()[0]
		votes = session.query(Vote).filter_by(userId=userId, postId=postId).all()

		if len(votes) == 0:
			vote = Vote(userId=userId, postId=postId, vote=voteValue)
			session.add(vote)
			post.voteTotal += voteValue
		else:
			vote = votes[0]
			currentVoteVal = vote.vote
			# Make adjust
			diff = currentVoteVal - voteValue
			post.voteTotal -= diff
			# Update value
			vote.vote = voteValue

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
api.add_resource(VotePost, '/post/vote')
api.add_resource(DeletePost, '/post/delete')

if __name__ == '__main__':
	app.run(debug=True, host='0.0.0.0', port=80)