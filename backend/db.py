from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy import ForeignKey, desc
from sqlalchemy.orm import relationship
from sqlalchemy import Column, Integer, String, Text, Table, Float, Boolean, TIMESTAMP
from sqlalchemy.dialects.mysql import DOUBLE

# Configure this line for database connection
engine = create_engine('mysql+mysqlconnector://root:root@localhost/toilet') #, echo=True)
Session = sessionmaker(bind=engine)
Base = declarative_base()

class User(Base):
	__tablename__ = 'users'
	id = Column(Integer, primary_key=True)
	posts = relationship("Post", uselist=True, backref="user")
	votes = relationship("Vote", uselist=True, backref="user")

class Post(Base):
	__tablename__ = 'posts'
	id = Column(Integer, primary_key=True)
	userId = Column(Integer, ForeignKey('users.id'))
	rating = Column(Integer)
	comment = Column(Text)
	time = Column(TIMESTAMP)
	voteTotal = Column(Integer)
	flagCount = Column(Integer)
	# Location details
	building = Column(String(3))
	floor = Column(Integer)

class Vote(Base):
	__tablename__ = 'votes'
	id = Column(Integer, primary_key=True)
	postId = Column(Integer, ForeignKey('posts.id'))
	userId = Column(Integer, ForeignKey('users.id'))
	vote = Column(Integer)

Base.metadata.create_all(engine)

def getSession():
	return Session()

# Test code

# session = getSession()

# Add a user
# user = User()
# session.add(user)

# Add a post
# post = Post(userId=1, rating=5, comment='This was great!', time='2017-01-05 12:37:00', voteTotal=5, flagCount=0, building='GDC', floor=2, locationDetailText='The good one')
# session.add(post)

# All posts for a user
# for p in session.query(Post).filter_by(userId=1).all():
# 	print (p.comment)

# Add random posts
# for i in range(5):
# 	post = Post(userId=1, rating=5, comment='This is a ranked post', time='2017-01-05 12:37:00', voteTotal=random.randint(0, 500), flagCount=0, building='GDC', floor=2, locationDetailText='The good one')
# 	session.add(post)

# Get all-time top posts
# for p in session.query(Post).order_by(desc(Post.voteTotal)).all():
# 	print (p.as_dict())

# Get most recent posts
# for p in session.query(Post).order_by(desc(Post.time)).all():
# 	print (p.time)

# Get trending posts
# This is top posts within the past 24 hours
# now = datetime.datetime.now()
# dayAgo = now - timedelta(days=1)

# for p in session.query(Post).order_by(desc(Post.voteTotal)).filter(Post.time >= dayAgo).all():
# 	print (p.as_dict())

# session.commit()

