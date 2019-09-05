#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import discord
from discord.ext import commands

import sqlite_mindustry #database stuff
import asyncio

with open('token.txt', 'r') as infile:
    data = [i.strip('\n') for i in infile.readlines()]
    TOKEN = data[0] #first line in token.txt
    SERVERID = int(data[1])
    DB = data[2]
'''
roles:
    #Registered
    #Admin
'''

client = commands.Bot(command_prefix = '..') 


@client.event
async def on_ready():
    sqlite_mindustry.make_db("test.db")
    print("bot is ready...")


@client.event
async def on_member_update(old, new):
    guild = client.get_guild(SERVERID)
    if guild and old.roles != new.roles and new.guild == guild:
        print(new.name + "-> db updated")
        admin_role = discord.utils.find(lambda r: r.name == "Admin", guild.roles)
        if admin_role in new.roles:
            sqlite_mindustry.changeAdmin(DB, 1, new.id)
        else:
            sqlite_mindustry.changeAdmin(DB, 0, new.id)
            

@client.command()
async def signup(ctx, login=None, password=None):
    info = '''
    Press !signup followed by your username and password (in this chat).
You are able to change them with !changelogin and see them with !showllogin.
**important:** The system isn't case sensitive, (only use lowercase ascii characters)
*We are storing some data to speed up the loginprocess.*
    '''
    guild = client.get_guild(SERVERID)
    if guild:
        registered_role = discord.utils.find(lambda r: r.name == "Registered", guild.roles)
    
    if not(isinstance(ctx.channel, discord.DMChannel)):
        await ctx.message.delete()
        try:
            await ctx.author.send(info)
            await ctx.send("Resume your sign up process in DM")
        except:
            await ctx.send("Please enable DM from server members.")
        finally:
            return
    elif login == None or password == None:
        await ctx.send(info)
    elif len(ctx.message.content.split(' ')) > 3:
        await ctx.send("Don't use spaces in your password or username")
        await ctx.send(info)
    elif sqlite_mindustry.check(DB, ctx.author.id, login):
        await ctx.send("already in DB or login already used")
    else:
        succes = sqlite_mindustry.insert(DB, ctx.author.id, login.lower(), password.lower(), 0)
        if succes:
            #msg = await client.send_message(channels[BOT_CHANNEL], 'success add role')
            #member = discord.utils.get(msg.server.members, id=ID)
            #role = discord.utils.get(msg.server.roles, name=ROLE)
            #await client.add_roles(member, role)
            await ctx.send('Success!')
            if registered_role:
                member = guild.get_member(ctx.author.id)
                await member.add_roles(registered_role)
        else:
            await ctx.send('Failed')
    

@client.command()
async def showlogin(ctx):
    if not(isinstance(ctx.channel, discord.DMChannel)):
        await ctx.message.delete()
    login, password = sqlite_mindustry.get_data(DB, ctx.author.id)[0]
    
    embed = discord.Embed(title="**Your account:**", colour=123123)
    embed.add_field(name='USERNAME:', value=login, inline=True)
    embed.add_field(name='PASSWORD:', value=password, inline=True)
    #embed.set_footer(text='''If you see "error", something went wrong, contact us.
    #                 This message will destroy itself after 60 seconds.''')
    try:
        await ctx.author.send(embed=embed)
        await asyncio.sleep(60)
    except:
        await ctx.send('Please enable DM from server members.')


@client.command()
async def changelogin(ctx, login=None, password=None):
    info="Press !changelogin followed by your new username and new password (in this chat).\n**important:** The system isn't case sensitive."
    if not(isinstance(ctx.channel, discord.DMChannel)):
        await ctx.message.delete()
        try:
            await ctx.author.send(info)
        except:
            await ctx.send('Please enable DM from server members.')
    elif login==None or password==None:
        await ctx.send(info)
    elif len(ctx.message.content.split(' ')) > 3:
        await ctx.send('**Don\'t use spaces in your password or username**')
        await ctx.send(info)
    else:
        succes = sqlite_mindustry.changeLogin(DB, ctx.author.id, login.lower(), password.lower())
        await ctx.send('Success!' if succes else 'Change login failed!')


@client.command()
async def deletelogin(ctx):
    if not(isinstance(ctx.channel, discord.DMChannel)):
        await ctx.message.delete()
    succes = sqlite_mindustry.delete(DB, ctx.message.author.id)
    try: 
        await ctx.author.send('Data deleted' if succes else 'Failed')
        guild = client.get_guild(SERVERID)
        if guild:
            registered_role = discord.utils.find(lambda r: r.name == "Registered", guild.roles)
            member = guild.get_member(ctx.author.id)
            await member.remove_roles(registered_role)
    except:
        pass


#shutdown
@client.command()
async def shutdown(ctx):
    if ctx.author.id == ctx.guild.owner_id:
        #give shutdown command via cmd
        #temp via file
        open("shutdown", "w").close()
        await client.close()

    
client.run(TOKEN)







    
