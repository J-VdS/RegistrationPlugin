import discord
from discord.ext import commands

import sqlite_mindustry #database stuff
import asyncio

with open('token.txt', 'r') as infile:
    data = [i.strip('\n') for i in infile.readlines()]
    TOKEN = data[0] #first line in token.txt
    OWNERID = data[1] #second line in token.txt
    

client = commands.Bot(command_prefix = '!') 
channels = {}


async def isowner(ctx):
    return (ctx.message.author.id == OWNERID) 


@client.event
async def on_ready():
    sqlite_mindustry.make_db("test.db")
        

@client.command()
async def signup(ctx, login=None, password=None):
    info = '''
    Press !signup followed by your username and password (in this chat).
You are able to change them with !changelogin and see them with !showllogin.
**important:** The system isn't case sensitive.
*We are storing some data to speed up the loginprocess.*
    '''
    #channels safety
    ID = ctx.message.author.id
    
    if ctx.message.channel in channels.values():
        await client.delete_message(ctx.message)
        try:
            await client.send_message(ctx.message.author, info)
            await client.say('Resume your sign up process in DM')
        except:
            await client.say('**ENABLE DM MESSAGES FROM SERVER MEMBERS!** \
                             \nOR YOU WON\'T BE ABLE TO MAKE AN ACCOUNT!')
    elif login==None or password==None:
        await client.say(info)
    elif len(ctx.message.content.split(' ')) > 3:
        await client.say('**Don\'t use spaces in your password or username**')
        await client.say(info)
    elif sqlite_mindustry.check(DB, ID, login):
        await client.say('already in database!')
    else:
        succes = sqlite_mindustry.insert(DB, ID, login.lower(), password.lower())
        if succes:
            msg = await client.send_message(channels[BOT_CHANNEL], 'success add role')
            member = discord.utils.get(msg.server.members, id=ID)
            role = discord.utils.get(msg.server.roles, name=ROLE)
            await client.add_roles(member, role)
            await client.delete_message(msg)
            await client.say('Success!')
        else:
            await client.say('Failed')
            
"""      
@client.command(pass_context=True,
                brief='-> shows your username and login',
                description='Press !showlogin in DM')
async def showlogin(ctx):
    author = ctx.message.author
    if ctx.message.channel in channels.values():
        await client.delete_message(ctx.message)
    login, password = sqlite_mindustry.get_data(DB, author.id)[0]
    embed = discord.Embed(title='**Your account:**',
                          colour=123123                          
                          )
    embed.add_field(name='USERNAME:', value=login, inline=True)
    embed.add_field(name='PASSWORD:', value=password, inline=True)
    embed.set_footer(text='''If you see "error", something went wrong, contact us.
                     This message will destroy itself after 60 seconds.''')
    try:
        msg = await client.send_message(author, embed=embed)
        await asyncio.sleep(60)
        await client.delete_message(msg)
    except:
        await client.say('**ENABLE DM MESSAGES FROM SERVER MEMBERS!** \
                         \nOR YOU WON\'T BE ABLE TO VIEW YOUR ACCOUNT!')
    
    

@client.command(pass_context=True,
                brief='-> change your username and password!',
                description='Press !changelogin in DM')
async def changelogin(ctx, login=None, password=None):
    info='''
    Press !changelogin followed by your new username and new password (in this chat).
**important:** The system isn't case sensitive.
    '''
    author = ctx.message.author
    if ctx.message.channel in channels.values():
        await client.delete_message(ctx.message)
        try:
            await client.send_message(author, info)
        except:
            await client.say('**ENABLE DM MESSAGES FROM SERVER MEMBERS!** \
                             \nOR YOU WON\'T BE ABLE TO CHANGE YOUR ACCOUNT!')
    elif login==None or password==None:
        await client.say(info)
    elif len(ctx.message.content.split(' ')) > 3:
        await client.say('**Don\'t use spaces in your password or username**')
        await client.say(info)
    else:
        succes = sqlite_mindustry.changeLogin(DB, author.id, login.lower(), password.lower())
        await client.say('Success!' if succes else 'Change login failed!')


@client.command(pass_context=True,
                brief='-> deletes your data from database (DM)')
async def deletelogin(ctx):
    if ctx.message.channel in channels.values():
        await client.delete_message(ctx.message)
    succes = sqlite_mindustry.delete(DB, ctx.message.author.id)
    try: 
        await client.send_message(ctx.message.author, 'Data deleted' if succes else 'Failed')
    except:
        pass
    
"""
#shutdown
@client.command()
async def shutdown(ctx):
    #give shutdown command via cmd
    #temp via file
    open("shutdown", "w").close()
    await client.close()

    
client.run(TOKEN)







    